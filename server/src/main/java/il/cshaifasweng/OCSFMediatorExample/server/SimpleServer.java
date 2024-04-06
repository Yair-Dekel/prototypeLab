package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;
import javafx.util.Pair;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SimpleServer extends AbstractServer {

    private static Session session;
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

    private List<ConnectionToClient> managerClients = new ArrayList<>(); // Maintain a list of manager clients
    private List<ConnectionToClient>AllmanagerClients = new ArrayList<>(); // Maintain a list of manager clients

    private List<ConnectionToClient> userClients = new ArrayList<>();
    private List<String> usernames = new ArrayList<>();

    private List<String> usernamesformanager = new ArrayList<>();

//    private List<Pair<Registered_user, ConnectionToClient>> LoggedInUsers = new ArrayList<>();

    public SimpleServer(int port) {
        super(port);

    }

    public List<Task> getAllTasks(Session session) {
        // Use HQL to retrieve all tasks
        Query<Task> query = session.createQuery("FROM Task", Task.class);
        return query.getResultList();
    }


    public String getUsername(ConnectionToClient client) {
        int index = userClients.indexOf(client);
        if (index != -1 && index < usernames.size()) {
            return usernames.get(index);
        }
        return null; // Return null if the ConnectionToClient is not found in the list
    }

    public String getUsernameManager(ConnectionToClient client) {
        int index = managerClients.indexOf(client);
        if (index != -1 && index < usernamesformanager.size()) {
            return usernamesformanager.get(index);
        }
        return null; // Return null if the ConnectionToClient is not found in the list
    }


    ConnectionToClient findManagerConnectionbyUser(Registered_user manager) {
        String username = manager.getUsername();
        int index = -1, i = 0;
        for (String managerUserName : usernamesformanager) {
            if (managerUserName.equals(username)) {
                index = i;
            }
            i++;
        }
        if (index > -1) {
            return managerClients.get(index);
        }
        return null;
    }

    ConnectionToClient findUserConnectionbyUser(Registered_user user) {
        String username = user.getUsername();
        int index = -1, i = 0;
        for (String UserName : usernames) {
            if (UserName.equals(username)) {
                index = i;
            }
            i++;
        }
        if (index > -1) {
            return userClients.get(index);
        }
        return null;
    }

    ConnectionToClient findManagerConnectionbyCommunity(Communities community) {
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();
            Query<Registered_user> query = session.createQuery(
                    "FROM Registered_user WHERE community = :communinty AND permission=true", Registered_user.class);
            query.setParameter("communinty", community);
            Registered_user user = query.uniqueResult();
            System.out.println(user.getUsername());
            tx2.commit();
            ConnectionToClient mc = findManagerConnectionbyUser(user);
            return mc;
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }


    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();

        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Registered_user.class);
        configuration.addAnnotatedClass(Task.class);
        configuration.addAnnotatedClass(Emergency_call.class);


        ServiceRegistry serviceRegistry = new
                StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        return configuration.buildSessionFactory(serviceRegistry);
    }


    public boolean CheckExistManagerClient(ConnectionToClient managerClient, String username) {
        boolean managerClientExists = false;
        boolean usernameExists = false;

        // Check if the manager client is already in the list
        for (ConnectionToClient existingManager : managerClients) {
            if (existingManager == managerClient) {
                managerClientExists = true;
                break;
            }
        }

        // Check if the username is already associated with another manager client
        for (String existingUsername : usernamesformanager) {
            if (existingUsername.equals(username)) {
                usernameExists = true;
                break;
            }
        }

        // Add the manager client only if it doesn't already exist and the username is unique
        return (!managerClientExists && !usernameExists);
    }

    public boolean CheckExistUserClient(ConnectionToClient userClient, String username) {
        boolean userClientExists = false;
        boolean usernameExists = false;

        // Check if the manager client is already in the list
        for (ConnectionToClient existingManager : userClients) {
            if (existingManager == userClient) {
                userClientExists = true;
                break;
            }
        }

        // Check if the username is already associated with another manager client
        for (String existingUsername : usernames) {
            if (existingUsername.equals(username)) {
                usernameExists = true;
                break;
            }
        }

        // Add the manager client only if it doesn't already exist and the username is unique
        return (!userClientExists && !usernameExists);
    }

    public void addManagerClient(ConnectionToClient managerClient, String username) {
        boolean managerClientExists = false;
        boolean usernameExists = false;

        // Check if the manager client is already in the list
        for (ConnectionToClient existingManager : managerClients) {
            if (existingManager == managerClient) {
                managerClientExists = true;
                break;
            }
        }


        // Check if the username is already associated with another manager client
        for (String existingUsername : usernamesformanager) {
            if (existingUsername.equals(username)) {
                usernameExists = true;
                break;
            }
        }

        // Add the manager client only if it doesn't already exist and the username is unique
        if (!managerClientExists && !usernameExists) {
            System.out.println("Adding a new manager to the list in the server");
            managerClients.add(managerClient);
            usernamesformanager.add(username);
            String name = getUsernameManager(managerClient);
            System.out.println("Manager username: " + name);

        } else {
            if (managerClientExists) {
                System.out.println("Manager client already exists");
            }
            if (usernameExists) {
                System.out.println("Username already exists for another manager: " + username);
            }
        }
        System.out.println("the size is:" + managerClients.size());
    }

    public void addAllManagerClient(ConnectionToClient managerClient) {
        if (!AllmanagerClients.contains(managerClient)) {
            System.out.println("Adding a new manager to the ALLLLLLLLL list in the server");
            AllmanagerClients.add(managerClient);
        }
        System.out.println("the size aLLLLL is:" + managerClients.size());
        System.out.println("out of add all managers");
    }

    public void addUserClient(ConnectionToClient UserClient, String username) {
        boolean userClientExists = false;
        boolean usernameExists = false;

        // Check if the manager client is already in the list
        for (ConnectionToClient existingUser : userClients) {
            if (existingUser == UserClient) {
                userClientExists = true;
                break;
            }
        }

        // Check if the username is already associated with another manager client
        for (String existingUsername : usernames) {
            if (existingUsername.equals(username)) {
                usernameExists = true;
                break;
            }
        }

        // Add the manager client only if it doesn't already exist and the username is unique
        if (!userClientExists && !usernameExists) {
            System.out.println("Adding a new User to the list in the server");
            userClients.add(UserClient);
            usernames.add(username);
            String name = getUsername(UserClient);
            System.out.println("User username: " + name);

        } else {
            if (userClientExists) {
                System.out.println("user client already exists");
            }
            if (usernameExists) {
                System.out.println("Username already exists for another user: " + username);
            }
        }
        System.out.println("the size is:" + userClients.size());
    }

    public void removeManagerClient(ConnectionToClient managerClient) {
        // Remove the manager client and its associated username
        int index = managerClients.indexOf(managerClient);
        if (index != -1 && index < usernamesformanager.size()) {
            String username = usernamesformanager.remove(index);
            System.out.println("Removing manager from the list in the server: " + username);
            managerClients.remove(index);
        }
    }

    public void removeUserClient(ConnectionToClient userClient) {
        // Remove the manager client and its associated username
        int index = userClients.indexOf(userClient);
        if (index != -1 && index < usernames.size()) {
            String username = usernames.remove(index);
            System.out.println("Removing user from the list in the server: " + username);
            userClients.remove(index);
        }
    }


    private void updateUserTasks(DisplayDataMessage datarequest) throws IOException {
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();
            Query<Task> query = session.createQuery("FROM Task WHERE registered_user.id = :userId", Task.class);
            query.setParameter("userId", datarequest.getUser().getId());
            List<Task> tasks = query.list();
            datarequest.setTasks(tasks);
            tx2.commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    private void updateUserTasks_done(DisplayDataMessage datarequest) throws IOException {
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();
            System.out.println("done");
            Query<Task> query = session.createQuery("FROM Task WHERE Volunteer.id = :userId AND Status= 'Completed'", Task.class);
            query.setParameter("userId", datarequest.getUser().getId());
            List<Task> tasks = query.list();
            datarequest.setTasks(tasks);
            tx2.commit();
        } catch (RuntimeException e) {
            if (tx2 != null) tx2.rollback();
            throw e;
        } finally {
            session.close(); // Close the second session
        }
    }

    public List<Task> getAllMyCompletedTasks(Session session, String username) {
        // Use HQL to retrieve tasks meeting the specified conditions
        Query<Task> query = session.createQuery(
                "SELECT t FROM Task t JOIN t.Volunteer v " +
                        "WHERE v.username = :username " +
                        "AND t.Status = 'In Process' " + // Filter by status
                        "ORDER BY t.completiontime ASC", Task.class);
        query.setParameter("username", username);

        try {
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    private void listviewForUserTOVolunteer(String username) throws IOException {
        System.out.println("in the listview func for volunteer " + username);
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();

            // Perform operations with the session
            System.out.println("in desplayyyyyyyy");
            for (ConnectionToClient user : userClients) {
                String clientUsername = getUsername(user);

                // Check if the client's username matches the provided username
                if (!clientUsername.equals(username)) {
                    List<Task> tasks = getAllWaitingTasks(session, clientUsername);
                    DisplayDataMessage dis = new DisplayDataMessage(tasks, "tasks");
                    user.sendToClient(dis);
                }
            }
            tx2.commit();
        } catch (RuntimeException e) {
            if (tx2 != null) tx2.rollback();
            throw e;
        } finally {
            session.close(); // Close the session
        }
    }


    private void listviewFromUser() throws IOException {


        System.out.println("in the listview func ");
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();

            // Perform operations with the second session
            for (ConnectionToClient user : managerClients) {
                String mangeruserClient = getUsernameManager(user);
                int i = 1;


                List<Task> tasks = getAllUnApprovedTasks(session, mangeruserClient);
                DisplayDataMessage dis = new DisplayDataMessage(tasks, "tasks");
                user.sendToClient(dis);
            }
            tx2.commit();
        } catch (RuntimeException e) {
            if (tx2 != null) tx2.rollback();
            throw e;
        } finally {
            session.close(); // Close the second session
        }
    }

    public void deadlineCheck() {
        // Get the current date
        LocalDateTime currentDate = LocalDateTime.now();

        try {
            SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
            session = sessionFactory.openSession();

            Transaction tx2 = null;
            try {
                tx2 = session.beginTransaction();
                // HQL query to select tasks with status other than "Completed" or "Not Completed" and deadline smaller than today
                Query<Task> query = session.createQuery(
                        "FROM Task WHERE Status NOT IN ('Completed', 'Not Completed') AND Deadline < :currentDate", Task.class);
                query.setParameter("currentDate", currentDate);
                // Execute the query
                List<Task> notCompleted = query.getResultList();

                // Check if the list is empty
                if (notCompleted.isEmpty()) {
                } else {
                    // Iterate over the result set
                    for (Task task : notCompleted) {
                        // Update the status to "Not Completed"
                        task.setStatus("Not Completed");
                        session.saveOrUpdate(task); // Update the task in the database
                    }
                }

                // Save the changes by committing the transaction
                tx2.commit();
            } catch (Exception e) {
                if (tx2 != null) {
                    tx2.rollback(); // Rollback the transaction in case of exception
                }
                e.printStackTrace(); // Handle the exception appropriately
            } finally {
                // Close the Hibernate session
                if (session != null) {
                    session.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Handle the exception appropriately
        }
    }

    public List<Task> getAllUnApprovedTasks(Session session, String username) {
        Communities headOfCommunity = getHeadOfCommunity(session, username);

        // Use HQL to retrieve tasks meeting the specified conditions
        Query<Task> query = session.createQuery(
                "SELECT t FROM Task t JOIN t.registered_user ru " +
                        "WHERE ru.username != :username AND ru.community = :community " +
                        "AND t.Status = :status " +
                        "ORDER BY t.Creation_time ASC", Task.class);
        query.setParameter("username", username);
        query.setParameter("community", headOfCommunity);
        query.setParameter("status", "waiting for approval");

        try {
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }

    }

    private void listviewForUserRequestedTasks(String username) throws IOException {
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();

            // Fetch tasks for the specified user
            List<Task> tasks = getAllMyRequestedTasks(session, username);
            DisplayDataMessage dis = new DisplayDataMessage(tasks, "Requested Tasks");
            // Send tasks to the user
            for (ConnectionToClient user : userClients) {
                String clientUsername = getUsername(user);
                if (clientUsername.equals(username)) {
                    user.sendToClient(dis);
                    break; // Assuming each user has a unique username, so we can exit the loop after sending the message
                }
            }
            tx2.commit();
        } catch (RuntimeException e) {
            if (tx2 != null) tx2.rollback();
            throw e;
        } finally {
            session.close(); // Close the session
        }
    }


    private void listviewForUserRequestedTasks() throws IOException {

        System.out.println("in the listview func for requested tasks ");
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();

            // Perform operations with the second session
            for (ConnectionToClient user : userClients) {
                String clientusername = getUsername(user);


                List<Task> tasks = getAllMyRequestedTasks(session, clientusername);
                DisplayDataMessage dis = new DisplayDataMessage(tasks, "Requested Tasks");
                user.sendToClient(dis);
            }
            tx2.commit();
        } catch (RuntimeException e) {
            if (tx2 != null) tx2.rollback();
            throw e;
        } finally {
            session.close(); // Close the second session
        }
    }


    private Communities getHeadOfCommunity(Session session, String username) {
        Query<Registered_user> query = session.createQuery(
                "FROM Registered_user WHERE username = :username", Registered_user.class);
        query.setParameter("username", username);
        Registered_user user = query.uniqueResult();
        if (user != null) {
            return user.getCommunity();
        } else {
            throw new IllegalArgumentException("User not found: " + username);
        }
    }

    public List<Task> getAllWaitingTasks(Session session, String username) {
        // Use HQL to retrieve all tasks
        System.out.println(" entered the waiting task");
        Communities Community = getCommunity(session, username);

        Query<Task> query = session.createQuery(
                "SELECT t FROM Task t JOIN t.registered_user ru " +
                        "WHERE ru.username != :username AND ru.community = :community " +
                        "AND t.Status = :status " +
                        "ORDER BY t.Creation_time ASC", Task.class);
        query.setParameter("username", username);
        query.setParameter("community", Community);
        query.setParameter("status", "waiting for volunteer");

        try {
            if (query.getResultList() == null || query.getResultList().isEmpty()) {
            }
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }


    }

    private Communities getCommunity(Session session, String username) {
        Query<Registered_user> query = session.createQuery(
                "FROM Registered_user WHERE username = :username", Registered_user.class);
        query.setParameter("username", username);
        Registered_user user = query.uniqueResult();
        System.out.println(user.getCommunity());
        if (user != null) {
            return user.getCommunity();
        } else {
            throw new IllegalArgumentException("User not found: " + username);
        }
    }

    public List<Task> getAllMyRequestedTasks(Session session, String username) {
        // Use HQL to retrieve tasks meeting the specified conditions
        Query<Task> query = session.createQuery(
                "SELECT t FROM Task t JOIN t.registered_user ru " +
                        "WHERE ru.username = :username " +
                        "ORDER BY t.Creation_time DESC", Task.class);
        query.setParameter("username", username);

        try {
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }


    public Registered_user getUsernameId(Session session, String username) {
        System.out.println("got inside getudername id user");
        try {
            // Query the database for the user with the provided username
            Query<Registered_user> query = session.createQuery(
                    "FROM Registered_user WHERE username = :username", Registered_user.class);
            query.setParameter("username", username);
            Registered_user user = query.uniqueResult();

            // Check if the user exists
            if (user != null) {
                // Return the user object
                return user;
            } else {
                // User not found, you can choose to return null or throw an exception
                // For example:
                // throw new IllegalArgumentException("User not found: " + username);
                return null;
            }
        } catch (HibernateException e) {
            // Handle Hibernate exceptions
            throw new RuntimeException("Error accessing database", e);
        }
    }


    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("get into handle from client in server class");
        Task myTask = null;
        String request = null;
        if (msg instanceof String) {
            request = (String) msg;
            if (request.equals("deadline check")) {
                deadlineCheck();
            }
        }

        else if (msg instanceof DisplayDataMessage) {
            DisplayDataMessage datarequest = (DisplayDataMessage) msg;

            if (datarequest.getDataType() != null && datarequest.getDataType().equals("uploaded")) {
                try {
                    updateUserTasks(datarequest);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            else if (datarequest.getDataType() != null && datarequest.getDataType().equals("performed")) {
                try {
                    updateUserTasks_done(datarequest);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (datarequest.getDataType() != null && datarequest.getDataType().equals("members")) {
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                Session session = null;
                try {
                    session = sessionFactory.openSession();
                    Transaction tx2 = null;
                    try {
                        tx2 = session.beginTransaction();
                        Communities communityName = datarequest.getCommunity();
                        // Fetch users from a specific community
                        Query<Registered_user> query = session.createQuery("FROM Registered_user WHERE community = :community AND permission = false", Registered_user.class);
                        query.setParameter("community", communityName);
                        List<Registered_user> members = query.list();
                        datarequest.setMembers(members);
                        tx2.commit();
                    } catch (Exception e) {
                        if (tx2 != null) {
                            tx2.rollback();
                        }
                        e.printStackTrace();
                    }
                } finally {
                    if (session != null) {
                        session.close();
                    }
                }
            }
            try {
                client.sendToClient(datarequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("b");
        } else if (msg instanceof MessageOfStatus) {
            request = ((MessageOfStatus) msg).getChangeStatus();
            myTask = ((MessageOfStatus) msg).getTask();

        } else if (msg instanceof NewTaskMessage) {
            System.out.println("msg recognized instanceof NewTaskMessage");
            NewTaskMessage ntm = (NewTaskMessage) msg;
            try {
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();
                session.beginTransaction();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime futureDeadline1 = now.plusDays(7);
                Task nt = new Task(ntm.getType(), ntm.getOpenby(), ntm.getDeadline(), ntm.getDetails());
                ntm.setNewTask(nt);
                session.save(nt);
                session.getTransaction().commit();
                System.out.println("in new task");
                listviewFromUser();
                DisplayDataMessage datarequest = new DisplayDataMessage(ntm.getOpenby(), "uploaded");
                updateUserTasks(datarequest);
                System.out.println("leen");
                client.sendToClient(ntm);
                findManagerConnectionbyCommunity(ntm.getOpenby().getCommunity()).sendToClient(datarequest);
            } catch (Exception exception) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
                exception.printStackTrace();

            } finally {
                session.close();
            }

        }

        //-----------------------------------------------------------------------------------------------------
        else if (msg instanceof NewEmergencyCall) {
            NewEmergencyCall ntm = (NewEmergencyCall) msg;
            System.out.println("we are in emergency call section brooooooo");
            System.out.println(ntm.getGiven_name());
            System.out.println(ntm.getPhone_number());


            try {
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();
                session.beginTransaction();
                LocalDateTime now = LocalDateTime.now();
                Emergency_call temp = new Emergency_call(ntm.getGiven_name(), ntm.getPhone_number(), ntm.getOpenby1(), ntm.getHost());
                session.save(temp);
                session.getTransaction().commit();
                int i=1;
                for (ConnectionToClient manager : AllmanagerClients) {
                    System.out.println("manager number : "+ i++);
                    listOfEmergency(manager);
                }
                System.out.println("successsssssdddddddddddddddddddddddddddddddddddddddddd");
            } catch (Exception exception) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
                exception.printStackTrace();

            } finally {
                session.close();
            }
        }

        //----------------------------------------------------------------------------------------------
        else if (msg instanceof Message) {
            if (((Message) msg).getMessage().equals("Confirm information")) {
                System.out.println("In server / Confirm information");

                String username = ((Message) msg).getUserName();
                String password = ((Message) msg).getPassword();
                System.out.println(username + "    " + password);

                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                Session session = null; // Declare session variable
                Transaction tx = null;

                try {
                    session = sessionFactory.openSession();
                    tx = session.beginTransaction();

                    System.out.println("Confirm");
                    List<Registered_user> users = session.createQuery("FROM Registered_user", Registered_user.class).getResultList();

                    Message message = null;
                    for (Registered_user user : users) {
                        if (user.getUsername().equals(username)) {
                            if (user.getPassword().equals(password)) {
                                if (CheckExistManagerClient(client, username) && CheckExistUserClient(client, username)) {
                                    message = new Message("correct");
                                    message.setUser(user);
                                } else {
                                    message = new Message("exists");
                                }
                                System.out.println("Correct credentials");
                            } else {
                                message = new Message("wrongPassword");
                                System.out.println("Incorrect password");
                            }
                            break; // Exit loop once user is found
                        }

                    }
                    if (message == null) {
                        message = new Message("userNotExist");
                        System.out.println("User does not exist");
                    }
                    client.sendToClient(message);
                    tx.commit();
                } catch (IOException e) {
                    // Handle IO exception
                    throw new RuntimeException("Error sending message to client", e);
                } catch (HibernateException e) {
                    // Handle Hibernate exception
                    if (tx != null) tx.rollback();
                    throw new RuntimeException("Error accessing database", e);
                } finally {
                    if (session != null) {
                        session.close(); // Close session in finally block
                    }
                }
            } else if (((Message) msg).getMessage().equals("list view")) {
                System.out.println("in list view");
                String username1 = ((Message) msg).getUserName();
                System.out.println(username1);
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session


                    List<Task> tasks = getAllUnApprovedTasks(session, username1);
                    System.out.println("in list view back from function");
                    if (tasks.isEmpty()) {
                        System.out.println("nothinggggggggggggggggggggggggggg");
                    }
                    DisplayDataMessage dis = new DisplayDataMessage(tasks, "tasks");
//                    System.out.println(dis.getTasks().get(0).getId());
                    client.sendToClient(dis);
                    tx2.commit();
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    session.close(); // Close the second session
                }
            } else if (((Message) msg).getMessage().equals("list view for volunteering")) {
                System.out.println("in list view volunteer!!");
                String username2 = ((Message) msg).getUserName();
                System.out.println(username2);
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session

                    List<Task> tasks = getAllWaitingTasks(session, username2);

                    if (tasks.isEmpty()) {
                        System.out.println("empty!!!!!!!");
                    } else {
                        for (Task task : tasks) {
                            System.out.println("1" + task.getType_of_task());
                        }
                    }
                    DisplayDataMessage dis = new DisplayDataMessage(tasks, "tasks");
                    client.sendToClient(dis);
                    tx2.commit();
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    session.close(); // Close the second session
                }


            } else if (((Message) msg).getMessage().equals("list view for requestedTasks")) {
                System.out.println("list view for requestedTasks");
                String username3 = ((Message) msg).getUserName();
                System.out.println(username3);
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session

                    List<Task> tasks = getAllMyRequestedTasks(session, username3);

                    if (tasks.isEmpty()) {
                        System.out.println("empty!!!!!!!");
                    } else {
                        for (Task task : tasks) {
                            System.out.println("1" + task.getType_of_task());
                        }
                    }
                    DisplayDataMessage dis = new DisplayDataMessage(tasks, "Requested Tasks");
                    client.sendToClient(dis);
                    tx2.commit();
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    session.close(); // Close the second session
                }


            } else if (((Message) msg).getMessage().equals("list view for volunteeredTasks")) {
                System.out.println("list view for VolunteeredTasks");
                String username3 = ((Message) msg).getUserName();
                System.out.println(username3);
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session

                    List<Task> tasks = getAllMyCompletedTasks(session, username3);

                    if (tasks.isEmpty()) {
                        System.out.println("empty!!!!!!!");
                    } else {
                        for (Task task : tasks) {
                            System.out.println("1" + task.getType_of_task());
                        }
                    }
                    DisplayDataMessage dis = new DisplayDataMessage(tasks, "Volunteered Tasks");
                    client.sendToClient(dis);
                    tx2.commit();
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    session.close(); // Close the second session
                }


            } else if (((Message) msg).getMessage().equals("log out manager")) {

                String username3 = ((Message) msg).getUserName();
                System.out.println("log out " + username3);
                System.out.println(username3);
                for (ConnectionToClient user1 : managerClients) {
                    System.out.println("inside for remove manager");
                    String clientmanagername = getUsernameManager(user1);
                    System.out.println("****" + clientmanagername);
                    if (clientmanagername.equals(username3)) {
                        removeManagerClient(user1);
                        System.out.println("Manager logged out: " + username3);
                        String clientmanagername1 = getUsernameManager(user1);
                        System.out.println("****" + clientmanagername1 + "****");
                        System.out.println("doneeeeeeeeeeeeeeeeeeeeeeee");

                        break; // No need to continue iterating once the client is found and removed
                    }
                }
            } else if (((Message) msg).getMessage().equals("log out user")) {

                String username3 = ((Message) msg).getUserName();
                System.out.println("log out " + username3);
                System.out.println(username3);
                for (ConnectionToClient user1 : userClients) {
                    System.out.println("inside for remove user");
                    String username = getUsername(user1);
                    System.out.println("****" + username);
                    if (username.equals(username3)) {
                        removeUserClient(user1);
                        System.out.println("User logged out: " + username3);
                        String clientname1 = getUsername(user1);
                        System.out.println("****" + clientname1 + "****");
                        System.out.println("doneeeeeeeeeeeeeeeeeeeeeeee");

                        break; // No need to continue iterating once the client is found and removed
                    }
                }

            } else if (((Message) msg).getMessage().equals("add manager client")) {
                System.out.println("add manger client in server");
                String username3 = ((Message) msg).getUserName();
                addManagerClient(client, username3);
                addAllManagerClient(client);
            } else if (((Message) msg).getMessage().equals("add user client")) {
                System.out.println("add uder client in server");
                String username3 = ((Message) msg).getUserName();
                addUserClient(client, username3);
            }
        }


        try {
            if (request.isBlank()) {
            } else if (request.equals("ShowEmergency")) {
                listOfEmergency(client);
            } else if (request.equals("accept")) {
                System.out.println("in accept");
                int id = myTask.getId();

                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session
                    Task task = session.get(Task.class, id);

                    // Check if the entity exists
                    if (task != null) {
                        // Modify the properties of the entity
                        task.setStatus("waiting for volunteer");

                        // Save the changes by committing the transaction
                        tx2.commit();
                        listviewForUserTOVolunteer(task.getRegistered_user().getUsername());
                        listviewForUserRequestedTasks();
                        MessageOfStatus message2 = new MessageOfStatus(task, "task accepted");
                        Registered_user openedBy = task.getRegistered_user();
                        ConnectionToClient addressee = findUserConnectionbyUser(openedBy);
                        if (addressee != null) {
                            addressee.sendToClient(new Notification("Task Accepted", openedBy));
                        }
                        client.sendToClient(message2);
                    }
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } finally {
                    session.close(); // Close the second session
                }
            } else if (request.startsWith("reject")) {
                System.out.println("in reject");
                int id = myTask.getId();

                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session
                    Task task = session.get(Task.class, id);

                    // Check if the entity exists
                    if (task != null) {
                        // Modify the properties of the entity
                        task.setStatus(request);

                        // Save the changes by committing the transaction
                        tx2.commit();
/*                        Registered_user openedBy = myTask.getRegistered_user();
                        ConnectionToClient addressee = findUserConnectionbyUser(openedBy);
                        addressee.sendToClient(new Notification("Task Rejected", openedBy));*/
                        listviewForUserRequestedTasks(task.getRegistered_user().getUsername());
                      //  listviewForUserRequestedTasks();
                        MessageOfStatus message2 = new MessageOfStatus(task, "task rejected");
                        // Echo back the received message to the client
                        client.sendToClient(message2);
                        tx2.commit();
                    }
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } finally {
                    session.close(); // Close the second session
                }

            } else if (request.equals("volunteering completed")) {
                int id = myTask.getId();
                String username10 = ((MessageOfStatus) msg).getUsername();
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session
                    Task task = session.get(Task.class, id);
                    Registered_user user = getUsernameId(session, username10);
                    // Check if the entity exists
                    if (task != null) {
                        // Modify the properties of the entity
                        task.setStatus("Completed");
                        task.setCompletiontime(LocalDateTime.now());
                        tx2.commit();
                        listviewForUserRequestedTasks(task.getRegistered_user().getUsername());
                        MessageOfStatus message2 = new MessageOfStatus(task, "volunteering done");
                        DisplayDataMessage datarequest = new DisplayDataMessage(task.getVolunteer(), "performed");
                        updateUserTasks_done(datarequest);
                        findManagerConnectionbyCommunity(task.getVolunteer().getCommunity()).sendToClient(datarequest);
                        client.sendToClient(message2);
                        tx2.commit();
                    }
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } finally {
                    session.close(); // Close the second session
                }
            } else if (request.equals("volunteering")) {
                int id = myTask.getId();
                String username10 = ((MessageOfStatus) msg).getUsername();
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session

                    Task task = session.get(Task.class, id);
                    Registered_user user = getUsernameId(session, username10);

                    // Check if the entity exists
                    if (task != null) {
                        // Modify the properties of the entity
                        task.setStatus("in process");
                        task.setVolunteer(user);
                        task.setCompletiontime(LocalDateTime.now());
                        tx2.commit();
                        listviewForUserRequestedTasks(task.getRegistered_user().getUsername());
                        listviewForUserTOVolunteer(task.getRegistered_user().getUsername());
                        MessageOfStatus message2 = new MessageOfStatus(task, "Thanks for volunteering");
                        // Echo back the received message to the client
                        client.sendToClient(message2);
                        tx2.commit();

                    }
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } finally {
                    session.close(); // Close the second session
                }


            }

        } catch (HibernateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void listOfEmergency(ConnectionToClient client) throws IOException {
        System.out.println("in listOfEmergency");
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();
        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();
            System.out.println("222222");
            List<Emergency_call> calls = getAllEmergency(session);
            DisplayCalls dis = new DisplayCalls(calls);

            client.sendToClient(dis);
            tx2.commit();
        } catch (RuntimeException e) {
            if (tx2 != null) tx2.rollback();
            throw e;
        } finally {
            session.close(); // Close the second session
        }
    }

    private List<Emergency_call> getAllEmergency(Session session) {
        Query<Emergency_call> query = session.createQuery("FROM Emergency_call ", Emergency_call.class);
        return query.getResultList();
    }
}
























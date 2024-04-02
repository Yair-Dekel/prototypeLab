package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SimpleServer extends AbstractServer {

    private static Session session;
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

    private List<ConnectionToClient> managerClients = new ArrayList<>(); // Maintain a list of manager clients
    private List<ConnectionToClient> userClients = new ArrayList<>();
    private List<String> usernames = new ArrayList<>();

    private List<String> usernamesformanager = new ArrayList<>();

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

    //    public void addManagerClient(ConnectionToClient managerClient) {
//        if (!managerClients.contains(managerClient)) {
//            System.out.println("Adding a new manager to the list in the server");
//            managerClients.add(managerClient);
//        }
//    }
    public void addManagerClient(ConnectionToClient managerClient, String username) {
        if (!managerClients.contains(managerClient)) {
            System.out.println("Adding a new manager to the list in the server");
            managerClients.add(managerClient);
            usernamesformanager.add(username);

        }
    }

    public void addUserClient(ConnectionToClient userClient, String username) {
        if (!userClients.contains(userClient)) {
            System.out.println("Adding a new user to the list in the server" + username);
            userClients.add(userClient);
            usernames.add(username);
        }
    }

    private void updateUserTasks(DisplayDataMessage datarequest) throws IOException {
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();
            System.out.println("in server uploaded");
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

    private void listviewForUserTOVolunteer(String username) throws IOException {

        System.out.println("in the listview func for volunteer " + username);
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();

            // Perform operations with the second session
            System.out.println("in desplayyyyyyyy");
            for (ConnectionToClient user : userClients) {
                System.out.println("inside for connected client");
                String clientusername = getUsername(user);
                System.out.println("****" + clientusername);


                List<Task> tasks = getAllWaitingTasks(session, clientusername);
                System.out.println("got the new list ");
                DisplayDataMessage dis = new DisplayDataMessage(tasks, "tasks");
                System.out.println("event event event");
                //for (ConnectionToClient user : userClients)
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


    private void listviewFromUser() throws IOException {
//
//        System.out.println("in the listview func ");
//        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
//        session = sessionFactory.openSession();
//
//        Transaction tx2 = null;
//        try {
//            tx2 = session.beginTransaction();
//
//            // Perform operations with the second session
//            System.out.println("in displayyyyyyyy");
//            List<Task> tasks = getAllUnApprovedTasks(session);
//            DisplayDataMessage dis = new DisplayDataMessage(tasks, "tasks");
//            for (ConnectionToClient manager : managerClients) {
//                manager.sendToClient(dis);
////                manager.sendToClient();
//            }
//            tx2.commit();
//        } catch (RuntimeException e) {
//            if (tx2 != null) tx2.rollback();
//            throw e;
//        } finally {
//            session.close(); // Close the second session
//        }

        System.out.println("in the listview func ");
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();

            // Perform operations with the second session
            System.out.println("in desplayyyyyyyy");
            for (ConnectionToClient user : managerClients) {
                System.out.println("inside for connected client");
                String mangeruserClient = getUsernameManager(user);
                System.out.println("****" + mangeruserClient);


                List<Task> tasks = getAllUnApprovedTasks(session, mangeruserClient);
                System.out.println("got the new list ");
                DisplayDataMessage dis = new DisplayDataMessage(tasks, "tasks");
                System.out.println("event event event");
                //for (ConnectionToClient user : userClients)
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


    public List<Task> getAllUnApprovedTasks(Session session, String username) {
//        // Use HQL to retrieve all tasks
//        NativeQuery<Task> query = session.createNativeQuery("SELECT * FROM Tasks WHERE Status = :status", Task.class);
//        query.setParameter("status", "waiting for approval");
//        return query.getResultList();
        // Find the head of the community associated with the provided username
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

    private void listviewForUserRequestedTasks() throws IOException {

        System.out.println("in the listview func for requested tasks ");
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();

            // Perform operations with the second session
            System.out.println("in desplayyyyyyyy");
            for (ConnectionToClient user : userClients) {
                System.out.println("inside for connected client in requested tasks");
                String clientusername = getUsername(user);
                System.out.println("****" + clientusername);


                List<Task> tasks = getAllMyRequestedTasks(session, clientusername);
                System.out.println("got the new list ");
                DisplayDataMessage dis = new DisplayDataMessage(tasks, "Requested Tasks");
                System.out.println("event event event");
                //for (ConnectionToClient user : userClients)
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
        System.out.println("in headofcommunity77777777777777777777777777");
        Query<Registered_user> query = session.createQuery(
                "FROM Registered_user WHERE username = :username", Registered_user.class);
        query.setParameter("username", username);
        Registered_user user = query.uniqueResult();
        if (user != null) {
            System.out.println(user.getCommunity() + "6666666666666666666666666666666");
            return user.getCommunity();
        } else {
            throw new IllegalArgumentException("User not found: " + username);
        }
    }

    public List<Task> getAllWaitingTasks(Session session, String username) {
        // Use HQL to retrieve all tasks
        System.out.println(" entered the waiting task");
        Communities Community = getCommunity(session, username);

        // Use HQL to retrieve tasks meeting the specified conditions
       /* Query<Task> query = session.createQuery(
                "SELECT t FROM Task t JOIN t.registered_user ru " +
                        "WHERE ru.username != :username AND ru.community = :community " +
                        "AND t.Status = :status"+*/
        Query<Task> query = session.createQuery(
                "SELECT t FROM Task t JOIN t.registered_user ru " +
                        "WHERE ru.username != :username AND ru.community = :community " +
                        "AND t.Status = :status " +
                        "ORDER BY t.Creation_time ASC", Task.class);
        query.setParameter("username", username);
        query.setParameter("community", Community);
        query.setParameter("status", "waiting for volunteer");

        try {
            System.out.println("out out out");
            if (query.getResultList() == null || query.getResultList().isEmpty()) {
                System.out.println("problem");
            }
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }


    }

    private Communities getCommunity(Session session, String username) {
        System.out.println("in community-----------------------------------------------");
        Query<Registered_user> query = session.createQuery(
                "FROM Registered_user WHERE username = :username", Registered_user.class);
        query.setParameter("username", username);
        System.out.println("im back hhhhhhhhhhhhhhhhhhh");
        Registered_user user = query.uniqueResult();
        System.out.println(user.getCommunity());
        if (user != null) {
            System.out.println(user.getCommunity() + "99999999999999999999999999999999999");
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
        }
        if (msg instanceof DisplayDataMessage) {
            DisplayDataMessage datarequest = (DisplayDataMessage) msg;
            System.out.println("a");

            if (datarequest.getDataType() != null && datarequest.getDataType().equals("uploaded")) {
                try {
                    updateUserTasks(datarequest);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
//                    System.out.println(tasks.get(0).getId());}
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
            System.out.println("hello00");
            request = ((MessageOfStatus) msg).getChangeStatus();
            myTask = ((MessageOfStatus) msg).getTask();

        } else if (msg instanceof NewTaskMessage) {
            System.out.println("msg recognized instanceof NewTaskMessage");
            NewTaskMessage ntm = (NewTaskMessage) msg;
//            AddToObMessage addtoOB = new AddToObMessage();
            try {
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();
                session.beginTransaction();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime futureDeadline1 = now.plusDays(7);
                Task nt = new Task(ntm.getType(), ntm.getOpenby(), ntm.getDeadline(), ntm.getDetails());
                ntm.setNewTask(nt);
//                addtoOB.setNewtask(nt);
                session.save(nt);
                session.getTransaction().commit();
                System.out.println("in new task");
                listviewFromUser();
                DisplayDataMessage datarequest = new DisplayDataMessage(ntm.getOpenby(), "uploaded");
                updateUserTasks(datarequest);
                for (ConnectionToClient manager : managerClients) {
                    manager.sendToClient(datarequest);
                }
            } catch (Exception exception) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
                System.err.println("An error occured, changes have been rolled back.");
                exception.printStackTrace();

            } finally {
                session.close();
            }
            try {
                client.sendToClient(ntm);
//                sendToAllClients(addtoOB);
            } catch (IOException e) {
                throw new RuntimeException(e);
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
                Emergency_call temp = new Emergency_call(ntm.getGiven_name(), ntm.getPhone_number(), ntm.getOpenby1());
                session.save(temp);
                session.getTransaction().commit();
                System.out.println("successsssssdddddddddddddddddddddddddddddddddddddddddd");
            } catch (Exception exception) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
                System.err.println("An error occured, changes have been rolled back. malekkkkkkkk");
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
                                message = new Message("correct");
                                message.setUser(user);
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
                    System.out.println(dis.getTasks().get(0).getId());
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
                System.out.println("im hereeeeeee");
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session
                    System.out.println("in desplayyyyyyyy volunteer");

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
                System.out.println("im inside requested tasks hereeeeeee");
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session
                    System.out.println("in desplayyyyyyyy requested tasks");

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


            } else if (((Message) msg).getMessage().equals("add manager client")) {
                System.out.println("add manger client in server");
                String username3 = ((Message) msg).getUserName();
                System.out.println(username3);
                addManagerClient(client, username3);
            }
        }


        try {
            if (request.isBlank()) {
                System.out.println("heyyy");
            } else if (request.equals("ShowEmergency")) {
                listOfEmergency(client);
            }
            else if (request.equals("add user client")) {
                System.out.println("enter here in request.equals(\"add user client\"))");
                String requestUser=((MessageOfStatus)msg).getUsername();
                System.out.println("check66666"+requestUser);
                addUserClient(client,requestUser);
            } else if (request.equals("accept")) {
                System.out.println("in accept");
                int id = myTask.getId();

                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session
                    System.out.println("in try accept");
                    Task task = session.get(Task.class, id);

                    // Check if the entity exists
                    if (task != null) {
                        // Modify the properties of the entity
                        task.setStatus("waiting for volunteer");
                        System.out.println("looking for volunteer");

                        // Save the changes by committing the transaction
                        tx2.commit();
                        System.out.println("ppppppppppppppppppppppppppppppppppppppppppppppppppp" + task.getRegistered_user().getUsername());
                        listviewForUserTOVolunteer(task.getRegistered_user().getUsername());
                        listviewForUserRequestedTasks();
                        MessageOfStatus message2 = new MessageOfStatus(task, "task accepted");
                        // Echo back the received message to the client
                        client.sendToClient(message2);
                        tx2.commit();
                        System.out.println("send to manager client");
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
                    System.out.println("in try reject");
                    Task task = session.get(Task.class, id);

                    // Check if the entity exists
                    if (task != null) {
                        // Modify the properties of the entity
                        task.setStatus(request);
                        System.out.println("rejected");

                        // Save the changes by committing the transaction
                        tx2.commit();
                        listviewForUserRequestedTasks();
                        MessageOfStatus message2 = new MessageOfStatus(task, "task rejected");
                        // Echo back the received message to the client
                        client.sendToClient(message2);
                        tx2.commit();
                        System.out.println("send to manager client");
                    }
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } finally {
                    session.close(); // Close the second session
                }
//            } else if (request.equals("get tasks")) {
//                System.out.println("in display");
//
//
//                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
//                session = sessionFactory.openSession();
//
//                Transaction tx2 = null;
//                try {
//                    tx2 = session.beginTransaction();
//
//                    // Perform operations with the second session
//
//                    List<Task> tasks = getAllTasks(session);
//                    for (Task task : tasks) {
//                        System.out.println(task.getType_of_task());
//                    }
//                    DisplayDataMessage dis = new DisplayDataMessage(tasks, "all tasks");
//                    System.out.println(dis.getTasks().get(0).getId());
//                    client.sendToClient(dis);
//                    tx2.commit();
//                } catch (RuntimeException e) {
//                    if (tx2 != null) tx2.rollback();
//                    throw e;
//                } finally {
//                    session.close(); // Close the second session
//                }
            } else if (request.equals("volunteering")) {
                System.out.println("in volunteering =================");
                int id = myTask.getId();
                String username10 = ((MessageOfStatus) msg).getUsername();
                System.out.println("try volunteer username" + username10);
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session
                    System.out.println("in try volunteering");
                    Task task = session.get(Task.class, id);
                    Registered_user user = getUsernameId(session, username10);
                    System.out.println("succesed ------------" + user.getId());
                    // Check if the entity exists
                    if (task != null) {
                        // Modify the properties of the entity
                        task.setStatus("in process");
                        task.setVolunteer(user);
                        System.out.println("someone has volunteer");

                        // Save the changes by committing the transaction
                        tx2.commit();

                        MessageOfStatus message2 = new MessageOfStatus(task, "Thanks for volunteering");
                        // Echo back the received message to the client
                        client.sendToClient(message2);
                        tx2.commit();
                        System.out.println("send to manager client");
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
        System.out.println("11");
        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();
            System.out.println("22");
            List<Emergency_call> calls = getAllEmergency(session);


            System.out.println("3333");
            for (Emergency_call call : calls) {
                System.out.println(call.getGiven_name());
                DisplayCalls dis = new DisplayCalls(calls);
                // System.out.println(dis.getTasks().get(0).getId());
                client.sendToClient(dis);
                tx2.commit();
            }
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
























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

    public SimpleServer(int port) {
        super(port);

    }

    public List<Task> getAllTasks(Session session) {
        // Use HQL to retrieve all tasks
        Query<Task> query = session.createQuery("FROM Task", Task.class);
        return query.getResultList();
    }

    //    private static List<Task> getAllPatient() throws Exception {
//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        System.out.println("List<Task> getAllPatient() throws Exception");
//        CriteriaQuery<Task> query = builder.createQuery(Task.class);
//        query.from(Task.class);
//        List<Task> data = session.createQuery(query).getResultList();
//        session.close();
//        return data;
//    }
    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();

        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Registered_user.class);
        configuration.addAnnotatedClass(Task.class);


        ServiceRegistry serviceRegistry = new
                StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        return configuration.buildSessionFactory(serviceRegistry);
    }



/*

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("get into handle from client in server class");
*/
/*                if (msg instanceof NewTaskMessage) {
                    System.out.println("msg recognized instanceof NewTaskMessage");
                    NewTaskMessage ntm = (NewTaskMessage) msg;
                    try {
                        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                        session = sessionFactory.openSession();
                        session.beginTransaction();
                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime futureDeadline1 = now.plusDays(7);
                        Task nt = new Task(ntm.getType(), ntm.getOpenby(), ntm.getDeadline(), ntm.getDetails());
                        session.save(nt);
                        session.getTransaction().commit();
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
                        *//*
     */
    /**//*
     */
/*ntm.setInDataBase(true);
                        client.sendToClient(ntm);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }*//*

                else if (msg instanceof Message) {
                    System.out.println("in sever /Confirm information ");


                    String username = ((Message) msg).getUserName();
                    String password = ((Message) msg).getPassword();
                    System.out.println(username + "    " + password);


                    SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                    session = sessionFactory.openSession();
                    Transaction tx2 = null;

                    try {
                        tx2 = session.beginTransaction();

                        // Perform operations with the second session
                        System.out.println("Confirm");
                        // Use a query to get all users
                        List<Registered_user> users = session.createQuery("FROM Registered_user", Registered_user.class).getResultList();
                        // Check if the entity exists
                        if (users != null) {

                            Message message2 = null;
                            for (Registered_user user : users) {

                                if (user.getUsername().equals(username)) {
                                    if (user.getPassword().equals(password)) {
                                        message2 = new Message("correct");
                                        message2.setUser(user);
                                        System.out.println("correct");

                                    } else {
                                        message2 = new Message("wrongPassword");
                                        System.out.println("wrongPassword");

                                    }
                                }
                            }
                            if (message2 == null)
                                message2 = new Message("user is not exist");
                            client.sendToClient(message2);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                System.out.println("in else");
            }


    }
}


*/

    private String getHeadOfCommunity(Session session, String username) {
        System.out.println("in headofcommunity77777777777777777777777777");
        Query<Registered_user> query = session.createQuery(
                "FROM Registered_user WHERE username = :username", Registered_user.class);
        query.setParameter("username", username);
        Registered_user user = query.uniqueResult();
        if (user != null) {
            System.out.println(user.getHeadOfCommunity()+"6666666666666666666666666666666");
            return user.getHeadOfCommunity();
        } else {
            throw new IllegalArgumentException("User not found: " + username);
        }
    }


    public List<Task> getAllUnApprovedTasks(Session session, String username) {
        // Find the head of the community associated with the provided username
        String headOfCommunity = getHeadOfCommunity(session, username);

        // Use HQL to retrieve tasks meeting the specified conditions
        Query<Task> query = session.createQuery(
                "SELECT t FROM Task t JOIN t.registered_user ru " +
                        "WHERE ru.username != :username AND ru.community = :community " +
                        "AND t.Status = :status", Task.class);
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



    public List<Task> getAllWaitingTasks(Session session,String username) {
        // Use HQL to retrieve all tasks
        System.out.println(" entered the waiting task");
        String Community = getCommunity(session, username);

        // Use HQL to retrieve tasks meeting the specified conditions
        Query<Task> query = session.createQuery(
                "SELECT t FROM Task t JOIN t.registered_user ru " +
                        "WHERE ru.username != :username AND ru.community = :community " +
                        "AND t.Status = :status", Task.class);
        query.setParameter("username", username);
        query.setParameter("community", Community);
        query.setParameter("status", "waiting for volunteer");

        try {
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }


    }

    private String getCommunity(Session session, String username) {
        System.out.println("in community-----------------------------------------------");
        Query<Registered_user> query = session.createQuery(
                "FROM Registered_user WHERE username = :username", Registered_user.class);
        query.setParameter("username", username);
        System.out.println("im back hhhhhhhhhhhhhhhhhhh");
        Registered_user user = query.uniqueResult();
        System.out.println(user.getCommunity());
        if (user != null) {
            System.out.println(user.getCommunity()+"99999999999999999999999999999999999");
            return user.getCommunity();
        } else {
            throw new IllegalArgumentException("User not found: " + username);
        }
    }


    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("get into handle from client in server class");
        Task myTask = null;
        String request = null;
        if (msg instanceof String) {
            request = (String) msg;
        } else if (msg instanceof MessageOfStatus) {
            System.out.println("hello00");
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
                session.save(nt);
                session.getTransaction().commit();
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } /*else if (msg instanceof Message) {
            System.out.println("in sever /Confirm information ");


            String username = ((Message) msg).getUserName();
            String password = ((Message) msg).getPassword();
            System.out.println(username + "    " + password);


            SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
            session = sessionFactory.openSession();
            Transaction tx2 = null;

            try {
                tx2 = session.beginTransaction();

                // Perform operations with the second session
                System.out.println("Confirm");
                // Use a query to get all users
                List<Registered_user> users = session.createQuery("FROM Registered_user", Registered_user.class).getResultList();
                // Check if the entity exists
                if (users != null) {

                    Message message2 = null;
                    for (Registered_user user : users) {

                        if (user.getUsername().equals(username)) {
                            if (user.getPassword().equals(password)) {
                                message2 = new Message("correct");
                                message2.setUser(user);
                                System.out.println("correct");

                            } else {
                                message2 = new Message("wrongPassword");
                                System.out.println("wrongPassword");

                            }
                        }
                    }
                    if (message2 == null)
                        message2 = new Message("user is not exist");
                    client.sendToClient(message2);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //////////////brak
        }*/
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
            }else if(((Message) msg).getMessage().equals("list view")) {
                System.out.println("in list view");
                String username1 = ((Message) msg).getUserName();
                System.out.println(username1);
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session


                    List<Task> tasks = getAllUnApprovedTasks(session,username1);
                    System.out.println("in list view back from function");
                    if (tasks.isEmpty()) {
                        System.out.println("nothinggggggggggggggggggggggggggg");
                    }
                    DisplayTasksMassage dis = new DisplayTasksMassage(tasks);
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
            }
            else if(((Message) msg).getMessage().equals("list view for volunteering")) {
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

                    if(tasks.isEmpty())
                    {
                        System.out.println("empty!!!!!!!");
                    }
                    else {
                        for (Task task : tasks) {
                            System.out.println("1" + task.getType_of_task());
                        }
                    }
                    DisplayTasksMassage dis = new DisplayTasksMassage(tasks);
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


            }
        }



        try {
            if (request.isBlank()) {
                System.out.println("heyyy");

            } else if (request.equals("change status")) {
                System.out.println("in change status");
                int id = myTask.getId();

                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session
                    System.out.println("in try");
                    Task task = session.get(Task.class, id);

                    // Check if the entity exists
                    if (task != null) {
                        // Modify the properties of the entity
                        task.setStatus("not performed yet");
                        System.out.println("not null");

                        // Save the changes by committing the transaction
                        tx2.commit();

                        MessageOfStatus message2 = new MessageOfStatus(task, "the change completed");
                        // Echo back the received message to the client

                        client.sendToClient(message2);
                        tx2.commit();
                        System.out.println("send to client");
                    }
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } finally {
                    session.close(); // Close the second session
                }
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
            } else if (request.equals("volunteering")) {
                System.out.println("in volunteering =================");
                int id = myTask.getId();

                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session
                    System.out.println("in try volunteering");
                    Task task = session.get(Task.class, id);

                    // Check if the entity exists
                    if (task != null) {
                        // Modify the properties of the entity
                        task.setStatus("in process");
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
            else if (request.equals("display tasks")) {
                System.out.println("in desplayyyyyyyy");


                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session
                    System.out.println("in desplayyyyyyyy");

                    List<Task> tasks = getAllTasks(session);
                    for (Task task : tasks) {
                        System.out.println(task.getType_of_task());
                    }
                    DisplayTasksMassage dis = new DisplayTasksMassage(tasks);
                    System.out.println(dis.getTasks().get(0).getId());
                    client.sendToClient(dis);
                    tx2.commit();
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } finally {
                    session.close(); // Close the second session
                }

            } /*
            else if (request.equals("list view for volunteering")) {
                System.out.println("in list view volunteer!!");
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                System.out.println("im hereeeeeee");
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session
                    System.out.println("in desplayyyyyyyy volunteer");

                    List<Task> tasks = getAllWaitingTasks(session);

                    if(tasks.isEmpty())
                    {
                        System.out.println("empty!!!!!!!");
                    }
                    for (Task task : tasks) {
                        System.out.println("1"+task.getType_of_task());
                    }
                    DisplayTasksMassage dis = new DisplayTasksMassage(tasks);
                    System.out.println(dis.getTasks().get(0).getId());
                    client.sendToClient(dis);
                    tx2.commit();
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } finally {
                    session.close(); // Close the second session
                }

            }*/

        } catch (HibernateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}




















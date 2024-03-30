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
    // Existing fields...

    private List<ConnectionToClient> managerClients = new ArrayList<>(); // Maintain a list of manager clients

    public SimpleServer(int port) {
        super(port);

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


    public List<Task> getAllUnApprovedTasks(Session session) {
        // Use HQL to retrieve all tasks
        NativeQuery<Task> query = session.createNativeQuery("SELECT * FROM Tasks WHERE Status = :status", Task.class);
        query.setParameter("status", "waiting for approval");
        return query.getResultList();
    }


    public List<Emergency_call> getAllEmergency(Session session) {
        // Use HQL to retrieve all emergency calls
        NativeQuery<Emergency_call> query = session.createNativeQuery("SELECT * FROM Emergency_call", Emergency_call.class);
        return query.getResultList();
    }

    public List<Task> getAllTasks(Session session) {
        // Use HQL to retrieve all tasks
        Query<Task> query = session.createQuery("FROM Task", Task.class);
        return query.getResultList();
    }

    private void listviewFromUser() throws IOException {

        System.out.println("in the listview func ");
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();

            // Perform operations with the second session
            System.out.println("in desplayyyyyyyy");

            List<Task> tasks = getAllUnApprovedTasks(session);
            DisplayTasksMassage dis = new DisplayTasksMassage(tasks);
            for (ConnectionToClient manager : managerClients)
                manager.sendToClient(dis);
            tx2.commit();
        } catch (RuntimeException e) {
            if (tx2 != null) tx2.rollback();
            throw e;
        } finally {
            session.close(); // Close the second session
        }
    }

    private void listview(ConnectionToClient client) throws IOException {

        System.out.println("in list view");
        System.out.println("enter here in request.equals(\"add manager client\"))");
        addManagerClient(client);
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();

            // Perform operations with the second session
            System.out.println("in desplayyyyyyyy");

            List<Task> tasks = getAllUnApprovedTasks(session);
            for (Task task : tasks) {
                System.out.println(task.getType_of_task());
            }
            DisplayTasksMassage dis = new DisplayTasksMassage(tasks);
            // System.out.println(dis.getTasks().get(0).getId());
            client.sendToClient(dis);
            tx2.commit();
        } catch (RuntimeException e) {
            if (tx2 != null) tx2.rollback();
            throw e;
        } finally {
            session.close(); // Close the second session
        }

    }


    private void listOfEmergency(ConnectionToClient client) throws IOException {
        System.out.println("in listOfEmergency");
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
        session = sessionFactory.openSession();

        Transaction tx2 = null;
        try {
            tx2 = session.beginTransaction();
            List<Emergency_call> calls = getAllEmergency(session);
            for (Emergency_call call : calls) {
                System.out.println(call.getGiven_name());
            }
            DisplayCalls dis = new DisplayCalls(calls);
            // System.out.println(dis.getTasks().get(0).getId());
            client.sendToClient(dis);
            tx2.commit();
        } catch (RuntimeException e) {
            if (tx2 != null) tx2.rollback();
            throw e;
        } finally {
            session.close(); // Close the second session
        }
    }

    private void handleStringMessage(String msg, ConnectionToClient client) throws IOException {
        String request = (String) msg;
        System.out.println("in string");
        if (request.isBlank()) {
            System.out.println("heyyy");
        }
        if (request.equals("add manager client")) {
            System.out.println("enter here in request.equals(\"add manager client\"))");
            addManagerClient(client);
        } else if (request.equals("display tasks")) {
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
                    // System.out.println(task.getType_of_task());
                }
                DisplayTasksMassage dis = new DisplayTasksMassage(tasks);
                // System.out.println(dis.getTasks().get(0).getId());
                client.sendToClient(dis);
                tx2.commit();
            } catch (RuntimeException e) {
                if (tx2 != null) tx2.rollback();
                throw e;
            } finally {
                session.close(); // Close the second session
            }

        } else if (request.equals("list view")) listview(client);
        else if (request.equals("ShowEmergency")) listOfEmergency(client);

    }

    private void handleEmergencyCall(NewEmergencyCall msg, ConnectionToClient client) {

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


        //----------------------------------------------------------------------------------------------
    }

    private void handleMessageOfStatus(MessageOfStatus msg, ConnectionToClient client) throws IOException {
        System.out.println("get into handle from client in server class");
        Task myTask = null;
        String request = null;
        System.out.println("hello00");
        request = ((MessageOfStatus) msg).getChangeStatus();
        myTask = ((MessageOfStatus) msg).getTask();

        if (request.equals("change status")) {
            System.out.println("in change status");
            int id = myTask.getId();

            SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
            session = sessionFactory.openSession();

            Transaction tx2 = null;
            try {
                tx2 = session.beginTransaction();

                // Perform operations with the second session
                // System.out.println("in try");
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
        }
    }

    private void handleMessage(Message msg, ConnectionToClient client) {

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


    private void handleNewTaskMessage(NewTaskMessage msg, ConnectionToClient client) {
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
            listviewFromUser(); ////////////////////////////////////////////////////////////////
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

    }

    public void addManagerClient(ConnectionToClient managerClient) {
        if (!managerClients.contains(managerClient)) {
            System.out.println("Adding a new manager to the list in the server");
            managerClients.add(managerClient);
        }
    }



    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        if (msg instanceof MessageOfStatus)
        {
            try {
                handleMessageOfStatus((MessageOfStatus) msg, client);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } //----------------------------------------------------------------------------------------------------------------------------------------
        else if (msg instanceof NewTaskMessage)
        {
            handleNewTaskMessage((NewTaskMessage) msg, client);
        }  //----------------------------------------------------------------------------------------------------------------------------------------
        else if (msg instanceof Message)
        {
            handleMessage((Message) msg, client);
        }//----------------------------------------------------------------------------------------------------------------------------------------
        else if (msg instanceof NewEmergencyCall) {
            handleEmergencyCall((NewEmergencyCall) msg, client);
        }
        { //----------------------------------------------------------------------------------------------------------------------------------------
            try {
                handleStringMessage((String) msg, client);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}




















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
    private static SessionFactory getSessionFactory() throws HibernateException
    {
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




    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("get into handle from client in server class");
//        Task myTask=null;
//        String request = null;
//        if(msg instanceof String)
//        {
//            System.out.println("recognized as string msg");
//            request = (String) msg;
//        } else if (msg instanceof MessageOfStatus) {
//            System.out.println("hello00");
//            request= ((MessageOfStatus) msg).getChangeStatus();
//            myTask=((MessageOfStatus) msg).getTask();
//
//        }
//        try {
//            if (request.isBlank()) {
//                System.out.println("heyyy");
//    /*            message.setMessage("Error! we got an empty message");
//                client.sendToClient(message);*/
//            }
//                /*

//                int entityId = message1.getTask().getId();
//                Configuration configuration = new Configuration().configure();
//                try (SessionFactory sessionFactory = configuration.buildSessionFactory();
//                     Session session = sessionFactory.openSession()) {
//
//                    // Begin a transaction
//                    Transaction transaction = session.beginTransaction();
//
//                    // Load the entity from the database
//                    Task task = session.get(Task.class, entityId);
//
//                    // Check if the entity exists
//                    if (task != null) {
//                        // Modify the properties of the entity
//                        task.setStatus("not performed yet");
//
//                        // Save the changes by committing the transaction
//                        transaction.commit();
//
//                        MessageOfStatus message2 = new MessageOfStatus(task, "the change completed");
//                        // Echo back the received message to the client
//
//                        client.sendToClient(message2);
//                    } else {
//                        System.out.println("Entity not found with id: " + entityId);
//                    }
//                }*/

//            else if (request.equals("change status")) {
//                System.out.println("in change status");
//                int id= myTask.getId();
//
//                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
//                session = sessionFactory.openSession();
//
//                Transaction tx2 = null;
//                try {
//                    tx2 = session.beginTransaction();
//
//                    // Perform operations with the second session
//                    System.out.println("in try");
//                    Task task = session.get(Task.class, id);
//
//                    // Check if the entity exists
//                    if (task != null) {
//                        // Modify the properties of the entity
//                        task.setStatus("not performed yet");
//                        System.out.println("not null");
//
//                        // Save the changes by committing the transaction
//                        tx2.commit();
//
//                        MessageOfStatus message2 = new MessageOfStatus(task, "the change completed");
//                        // Echo back the received message to the client
//
//                        client.sendToClient(message2);
//                        tx2.commit();
//                        System.out.println("send to client");
//                    }
//                } catch (RuntimeException e) {
//                    if (tx2 != null) tx2.rollback();
//                    throw e;
//                } finally {
//                    session.close(); // Close the second session
//                }
//            }
//
//                else if (request.equals("display tasks")) {
//                System.out.println("in displayyyyyyyy");
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
//                    System.out.println("in desplayyyyyyyy");
//
//                    List<Task> tasks = getAllTasks(session);
//                    for (Task task : tasks) {
//                        System.out.println(task.getType_of_task());
//                    }
//                    DisplayTasksMassage dis=new DisplayTasksMassage(tasks);
//                    System.out.println(dis.getTasks().get(0).getId());
//                    client.sendToClient(dis);
////                    SimpleClient.getClient().sen
//                    tx2.commit();
//                } catch (RuntimeException e) {
//                    if (tx2 != null) tx2.rollback();
//                    throw e;
//                } finally {
//                    session.close(); // Close the second session
//                }}

                if (msg instanceof NewTaskMessage) {
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
                        /**/ntm.setInDataBase(true);
                        client.sendToClient(ntm);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                System.out.println("in else");
            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

	/*public void sendToAllClients(Message message) {
		try {
			for (SubscribedClient SubscribedClient : SubscribersList) {
				SubscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}*/

    }
}
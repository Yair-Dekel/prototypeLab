package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.util.ArrayList;

import java.util.List;

import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import org.hibernate.service.ServiceRegistry;

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

    public List<Task> getAllUnApprovedTasks(Session session) {
        // Use HQL to retrieve all tasks
        NativeQuery<Task> query = session.createNativeQuery("SELECT * FROM Tasks WHERE Status = :status", Task.class);
        query.setParameter("status", "waiting for approval");
        return query.getResultList();
    }


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
        Task myTask=null;
        String request = null;
        if(msg instanceof String)
        {
            request = (String) msg;
        } else if (msg instanceof MessageOfStatus) {
            System.out.println("hello00");
            request= ((MessageOfStatus) msg).getChangeStatus();
            myTask=((MessageOfStatus) msg).getTask();

        }
        try {
            if (request.isBlank()) {
               System.out.println("heyyy");

           }


            else if (request.equals("change status")) {
                System.out.println("in change status");
                int id= myTask.getId();

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
            }

            else if (request.equals("accept")) {
                System.out.println("in accept");
                int id= myTask.getId();

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
            }

            else if (request.startsWith("reject")) {
                System.out.println("in reject");
                int id= myTask.getId();

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
                    DisplayTasksMassage dis=new DisplayTasksMassage(tasks);
                    System.out.println(dis.getTasks().get(0).getId());
                    client.sendToClient(dis);
                    tx2.commit();
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } finally {
                    session.close(); // Close the second session
                }

            }

            else if (request.equals("list view")) {
                System.out.println("in list view");
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
                    DisplayTasksMassage dis=new DisplayTasksMassage(tasks);
                    System.out.println(dis.getTasks().get(0).getId());
                    client.sendToClient(dis);
                    tx2.commit();
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } finally {
                    session.close(); // Close the second session
                }

            }

            else {
                System.out.println("in else");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



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
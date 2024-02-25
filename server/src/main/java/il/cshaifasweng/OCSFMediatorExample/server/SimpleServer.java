package il.cshaifasweng.OCSFMediatorExample.server;

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

    private static List<Task> getAllPatient() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        System.out.println("List<Task> getAllPatient() throws Exception");

        CriteriaQuery<Task> query = builder.createQuery(Task.class);
        query.from(Task.class);
        List<Task> data = session.createQuery(query).getResultList();
        session.close();
        return data;
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
        /*        MessageOfStatus message1 = (MessageOfStatus) msg;*/
//        Message message = (Message) msg;
        String request = (String) msg;
        try {
//            if (request.isBlank()) {
//                System.out.println("heyyy");
//                message.setMessage("Error! we got an empty message");
//                client.sendToClient(message);
//            }
/*            if (message1.getChangeStatus().startsWith("change status")) {
                int entityId = message1.getTask().getId();

                Configuration configuration = new Configuration().configure();
                try (SessionFactory sessionFactory = configuration.buildSessionFactory();
                     Session session = sessionFactory.openSession()) {

                    // Begin a transaction
                    Transaction transaction = session.beginTransaction();

                    // Load the entity from the database
                    Task task = session.get(Task.class, entityId);

                    // Check if the entity exists
                    if (task != null) {
                        // Modify the properties of the entity
                        task.setStatus("not performed yet");

                        // Save the changes by committing the transaction
                        transaction.commit();

                        MessageOfStatus message2 = new MessageOfStatus(task, "the change completed");
                        // Echo back the received message to the client

                        client.sendToClient(message2);
                    } else {
                        System.out.println("Entity not found with id: " + entityId);
                    }
                }
            } */
            if (request.equals("display tasks")) {
                System.out.println("in desplayyyyyyyy");
/*
             Configuration configuration = new Configuration().configure();

                try (
                        SessionFactory sessionFactory = configuration.buildSessionFactory();
                     Session session = sessionFactory.openSession()) {
                    System.out.println("trying send to client4444");

                    // Begin a transaction
                    Transaction transaction = session.beginTransaction();
                    System.out.println("trying send to client33");

                    // Create a CriteriaBuilder and CriteriaQuery
                    CriteriaBuilder builder = session.getCriteriaBuilder();
                    CriteriaQuery<Task> query = builder.createQuery(Task.class);
                    Root<Task> root = query.from(Task.class);
                    System.out.println("trying send to client2222");

                    // Set the query to select all tasks
                    query.select(root);

                    // Execute query and retrieve results
                    List<Task> results = session.createQuery(query).getResultList();

                    // Commit the transaction
                    transaction.commit();
                    // Send a message to the client indicating no tasks
                    System.out.println("trying send to client11");

                    client.sendToClient(results);
                    System.out.println("trying send to client");

                }

                SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

                // Create Hibernate session
                try (Session session = sessionFactory.openSession()) {
                    System.out.println("in desplayyyyyyyy");

                    List<Task> tasks = getAllTasks(session);
                    for (Task task : tasks) {
                        System.out.println(task.getId());
                    }
                    client.sendToClient(tasks);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Close the session factory when done
                //sessionFactory.close();


                List<Task> tasks = null;
                try {
                    tasks = getAllPatient();

                    for (Task task : tasks) {
                        System.out.println(task.getId());
                    }
                    client.sendToClient(tasks);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }*/
                SessionFactory sessionFactory = FactoryUtil.getSessionFactory();
                session = sessionFactory.openSession();

                Transaction tx2 = null;
                try {
                    tx2 = session.beginTransaction();

                    // Perform operations with the second session
                    System.out.println("in desplayyyyyyyy");

                    List<Task> tasks = getAllTasks(session);
                    for (Task task : tasks) {
                        System.out.println(task.getId());
                    }
                    client.sendToClient(tasks);
                    tx2.commit();
                } catch (RuntimeException e) {
                    if (tx2 != null) tx2.rollback();
                    throw e;
                } finally {
                    session.close(); // Close the second session
                }

            } else {
                System.out.println("in else");
            }


//					} else {
//					//add code here to send received message to all clients.
//					//The string we received in the message is the message we will send back to all clients subscribed.
//					//Example:
//					// message received: "Good morning"
//					// message sent: "Good morning"
//					//see code for changing submitters IDs for help
//					message.setMessage(request);
//					sendToAllClients(message);
//				}
//
//			}
       /* } catch (IOException e1) {
            e1.printStackTrace();//
        }*/
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
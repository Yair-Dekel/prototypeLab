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
    /*            message.setMessage("Error! we got an empty message");
                client.sendToClient(message);*/
           }
            if (request.equals("change status")) {
                System.out.println("this enter to change status ********************");

            }
            if (request.equals("display tasks")) {
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
//                    SimpleClient.getClient().sen
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
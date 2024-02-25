package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class SimpleServer extends AbstractServer {
    private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

    public SimpleServer(int port) {
        super(port);

    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("get into handle from client in server class");
/*        MessageOfStatus message1 = (MessageOfStatus) msg;*/
//        Message message = (Message) msg;
        String request = (String)msg;
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
            } */if (request.equals("display tasks")) {
                System.out.println("in desplayyyyyyyy");
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
        } catch (IOException e1) {
            e1.printStackTrace();//
        }
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

//package il.cshaifasweng.OCSFMediatorExample.server;
//
//import il.cshaifasweng.OCSFMediatorExample.entities.Message;
//import il.cshaifasweng.OCSFMediatorExample.entities.Registered_user;
//import il.cshaifasweng.OCSFMediatorExample.entities.Task;
//import org.hibernate.HibernateException;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import org.hibernate.cfg.Configuration;
//import org.hibernate.service.ServiceRegistry;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//public class SimpleChatServer {
//
//    private static Session session;
//    private static final ArrayList<Registered_user> registered_users = new ArrayList<>();
//    private static final ArrayList<Task> tasks = new ArrayList<>();
//
//
//    private static SessionFactory getSessionFactory() throws HibernateException {
//        Configuration configuration = new Configuration();
//        // Add ALL of your entities here. You can also try adding a whole package.
//        configuration.addAnnotatedClass(Task.class);
//        configuration.addAnnotatedClass(Registered_user.class);
//        configuration.addAnnotatedClass(Message.class);
//        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
//        return configuration.buildSessionFactory(serviceRegistry);
//    }
//
//
//    /**
//     * Initialize application's users
//     */
///*    private static void initializeUsers() {
//    Registered_user user1 = new Registered_user("Rom", "Levi", "rom_levi1", "123",  "0507773121", "Haifa");
//        session.save(user1);
//        session.flush();
//        Registered_user user2 = new Registered_user("Yarin", "Rabinobi", "yarin_rabinobi2", "1234",  "0524373191", "Tel-Aviv");
//        session.save(user2);
//        session.flush();
//        Registered_user user3 = new Registered_user("Dan", "Shimoni", "dan_shimoni1", "1235",  "0547373199", "Haifa");
//        session.save(user3);
//        session.flush();
//        Registered_user user4 = new Registered_user("Linoy", "Ohaion", "linoyOhaion2", "1232",  "0502213188", "Jerusalem");
//        session.save(user4);
//        session.flush();
//        Registered_user user5 = new Registered_user("Roman", "Shapira", "romanroman", "1231",  "0521153111", "Jerusalem");
//        session.save(user5);
//        session.flush();
//        Registered_user user6 = new Registered_user("Shira", "Omer", "ShiraOmer22", "1220", "0502479900", "Haifa");
//        session.save(user6);
//        session.flush();
//        Registered_user user7 = new Registered_user("Yarden", "Mesgav", "yarden_yarden3", "1230",  "0502479900", "Tel-Aviv");
//        session.save(user7);
//        session.flush();
//    }*/
//    private static void initializeUsers() {
//        Registered_user user1 = new Registered_user();
//        session.save(user1);
//        session.flush();
//        Registered_user user2 = new Registered_user();
//        session.save(user2);
//        session.flush();
//        Registered_user user3 = new Registered_user();
//        session.save(user3);
//        session.flush();
//        Registered_user user4 = new Registered_user();
//        session.save(user4);
//        session.flush();
//        Registered_user user5 = new Registered_user();
//        session.save(user5);
//        session.flush();
//        Registered_user user6 = new Registered_user();
//        session.save(user6);
//        session.flush();
//        Registered_user user7 = new Registered_user();
//        session.save(user7);
//        session.flush();
//    }
//
//    private static List<Registered_user> getUsers() throws Exception {
//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        CriteriaQuery<Registered_user> query = builder.createQuery(Registered_user.class);
//        query.from(Registered_user.class);
//        return session.createQuery(query).getResultList();
//    }
//
//    /**
//     * Initialize application's tasks
//     */
//    private static void initializeTask() throws Exception {
//        LocalTime currentTime = LocalTime.now();
//        LocalDateTime now = LocalDateTime.now();
//
//        LocalDateTime futureDeadline1 = now.plusDays(7);
//        Task task1 = new Task("Help with supermarket shopping", getUsers().get(0), futureDeadline1);
//
//
//        LocalDateTime futureDeadline2 = now.plusDays(14);
//        Task task2 = new Task("Ordering medication", getUsers().get(1), futureDeadline2);
//
//
//        LocalDateTime futureDeadline3 = now.plusDays(10);
//        Task task3 = new Task("A ride somewhere", getUsers().get(2), futureDeadline3);
//
//
//        LocalDateTime futureDeadline4 = now.plusDays(3);
//        Task task4 = new Task("Ordering medication", getUsers().get(3), futureDeadline3);
//
//
//        LocalDateTime futureDeadline5 = now.plusDays(1);
//        Task task5 = new Task("Help with supermarket shopping", getUsers().get(4), futureDeadline3);
//
//
//        tasks.add(task1);
//        tasks.add(task2);
//        tasks.add(task3);
//        tasks.add(task4);
//        tasks.add(task5);
//        tasks.forEach(task -> session.save(task));
//        session.flush();//rina add
//
//    }
//
//    private static List<Task> getTasks() throws Exception {
//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        CriteriaQuery<Task> query = builder.createQuery(Task.class);
//        query.from(Task.class);
//        List<Task> data = session.createQuery(query).getResultList();
//        return data;
//    }
//
//
//    public static void main(String[] args) throws Exception {
//        try {
//            SessionFactory sessionFactory = getSessionFactory();
//            session = sessionFactory.openSession();
//            session.beginTransaction(); // Begin transaction here
//            initializeUsers();
//            initializeTask();
//
//            // Commit the transaction after completing database operations
//            session.getTransaction().commit();
//
//
//            SimpleServer server = new SimpleServer(3000);
//            System.out.println("Server is listening");
//            server.listen();
//
//
//
//
//        } catch (Exception exception) {
//            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
//                session.getTransaction().rollback(); // Rollback transaction on exception
//            }
//            System.err.println("An error occurred, changes have been rolled back.");
//            exception.printStackTrace();
//        } finally {
//            if (session != null) {
//                session.close(); // Close session in the finally block
//            }
//        }
//
//    }
//
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public static int getRandomNumber(int min, int max) {
//        return (int) ((Math.random() * (max - min)) + min);
//    }
//}

package il.cshaifasweng.OCSFMediatorExample.server;



import il.cshaifasweng.OCSFMediatorExample.entities.Communities;
import il.cshaifasweng.OCSFMediatorExample.entities.Registered_user;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;

import il.cshaifasweng.OCSFMediatorExample.entities.TaskType;
import org.hibernate.SessionFactory;

import java.io.IOException;


import org.hibernate.Session;

import java.time.LocalDateTime;

public class SimpleChatServer {


    private static Session session;
    private static SimpleServer server;

 /*   private static SessionFactory getSessionFactory() throws HibernateException
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
    }*/


    public static void main(String[] args) throws IOException {
        server = new SimpleServer(3000);

        DataBaseCheck DB = new DataBaseCheck();
        SessionFactory sessionFactory = FactoryUtil.getSessionFactory();

        if (!DB.isDatabaseNotEmpty()) {
            try {
                session = sessionFactory.openSession();
                session.beginTransaction();
                System.out.println("1");

                Registered_user user1 = new Registered_user("Rom","Levi","rom_levi1","123",true,"0507773121", Communities.AHUZA);
                Registered_user user2 = new Registered_user("Yarin","Rabinobi","yarin_rabinobi2","1234",false,"0524373191",Communities.BAT_GALIM);
                Registered_user user3 = new Registered_user("Dan","Shimoni","dan_shimoni1","1235",false,"0547373199",Communities.ROMEMA);
                Registered_user user4 = new Registered_user("Linoy","Ohaion","linoyOhaion2","111",true,"0502213188",Communities.ROMEMA);
                Registered_user user5 = new Registered_user("Roman","Shapira","romanroman","1231",false,"0521153111",Communities.ROMEMA);
                Registered_user user6 = new Registered_user("Shira","Omer","ShiraOmer22","1220",false,"0502479900",Communities.ROMEMA);
                Registered_user user7 = new Registered_user("Yarden","Mesgav","yarden_yarden3","1230",false,"0532251580",Communities.ROMEMA);


                session.save(user1);
                session.save(user2);
                session.save(user3);
                session.save(user4);
                session.save(user5);
                session.save(user6);
                session.save(user7);
                //  session.getTransaction().commit(); // Save everything.

                System.out.println("2");


                session.flush();

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime past =now.minusDays(2);
                LocalDateTime futureDeadline1 = now.plusDays(7);
                Task t1 = new Task(TaskType.BABYSITTING,user1 , futureDeadline1, "in my house loaction: horev 10");
                LocalDateTime futureDeadline2 = now.plusDays(4);
                Task t2 = new Task(TaskType.CAR_CLEANING,user2 , futureDeadline2);
                LocalDateTime futureDeadline3 = now.plusDays(12);
                Task t3 = new Task(TaskType.DOG_WALKING,user3 , futureDeadline3);
                LocalDateTime futureDeadline4 = now.plusDays(1);
                Task t4 = new Task(TaskType.RIDE,user4 , futureDeadline4);
                LocalDateTime futureDeadline5 = now.plusDays(10);
                Task t5 = new Task(TaskType.YARD_WORK,user5 , past);





                System.out.println("3");


                session.save(t1);
                System.out.println("4");


                session.save(t2);
                session.save(t3);
                session.save(t4);
                session.save(t5);

                session.flush();
                System.out.println("5");

                session.getTransaction().commit(); // Save everything.
            } catch (Exception exception) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
                System.err.println("An error occured, changes have been rolled back.");
                exception.printStackTrace();
            } finally {
                session.close();
            }

        }
        System.out.println("server is listening");
        server.listen();
    }
}
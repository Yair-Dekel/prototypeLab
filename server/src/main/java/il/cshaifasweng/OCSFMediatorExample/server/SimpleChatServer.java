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
                Registered_user user2 = new Registered_user("rina","Rabinobi","rina","111",false,"0524373191",Communities.BAT_GALIM);
                Registered_user user8 = new Registered_user("abd","Rabinobi","abd","111",false,"0524373191",Communities.BAT_GALIM);
                Registered_user user3 = new Registered_user("Dan","Shimoni","dan_shimoni1","1235",false,"0547373199",Communities.ROMEMA);
                Registered_user user4 = new Registered_user("mhmd","Ohaion","mhmd","111",true,"0502213188",Communities.BAT_GALIM);
                Registered_user user5 = new Registered_user("Roman","Shapira","romanroman","1231",false,"0521153111",Communities.ROMEMA);
                Registered_user user6 = new Registered_user("Shira","Omer","ShiraOmer22","1220",false,"0502479900",Communities.ROMEMA);
                Registered_user user7 = new Registered_user("Yarden","Mesgav","yarden_yarden3","1230",false,"0532251580",Communities.ROMEMA);
                Registered_user user9 = new Registered_user("mais","Ohaion","mais","111",true,"0502213188",Communities.ROMEMA);
                Registered_user user10 = new Registered_user("Vered","Lir","verduni","1234",false,"0508653121", Communities.ROMEMA);

//////////////////////
                Registered_user user11 = new Registered_user("Rina","Samir","r_samir1","123",false,"0507653121", Communities.AHUZA);
                Registered_user user12 = new Registered_user("Soheir","Merei","sohirMer2","1234",false,"0538173191",Communities.AHUZA);
                Registered_user user13 = new Registered_user("Sonya","Samir","soni_s","1235",false,"054856249",Communities.AHUZA);
                Registered_user user14 = new Registered_user("Rina","Lior","lior123","111",false,"050275188",Communities.RAMAT_BEGIN);
                Registered_user user15 = new Registered_user("Gihad","Abdallah","abdalji1","1231",false,"053928111",Communities.RAMAT_BEGIN);
                Registered_user user16 = new Registered_user("Aisha","Abdallah","aishallh","1220",false,"050964200",Communities.RAMAT_BEGIN);
                Registered_user user17 = new Registered_user("Ida","Saleh","salda87","1230",true,"0533256485",Communities.RAMAT_BEGIN);
                Registered_user user18 = new Registered_user("Lilach","Cohen","lili2000","111",true,"055648288",Communities.DENIA);
                Registered_user user19 = new Registered_user("Gihad","Cohen","abdalji1","1231",false,"054448111",Communities.DENIA);
                Registered_user user20 = new Registered_user("Shuki","Jermans","shoosh","98",false,"055648737",Communities.DENIA);
                Registered_user user21 = new Registered_user("Yair","Dell","Yair","1231",false,"054448311",Communities.DENIA);
                Registered_user user22 = new Registered_user("Shir","David","shirush","050",true,"0533296485",Communities.RAMAT_ESCHOL);
                Registered_user user23 = new Registered_user("Shani","Noy","saniki","12345",false,"055848288",Communities.RAMAT_ESCHOL);
                Registered_user user24 = new Registered_user("Ron","Hayon","rono","1231",false,"059848111",Communities.RAMAT_ESCHOL);
                Registered_user user25 = new Registered_user("Snir","Avraham","sa97","97",false,"055621737",Communities.RAMAT_ESCHOL);
                Registered_user user26 = new Registered_user("Idan","Hili","Budu","1239",false,"050648311",Communities.RAMAT_ESCHOL);
                session.save(user11);
                session.save(user12);
                session.save(user13);
                session.save(user14);
                session.save(user15);
                session.save(user16);
                session.save(user17);
                session.save(user18);
                session.save(user19);

                session.save(user20);
                session.save(user21);
                session.save(user22);
                session.save(user23);
                session.save(user24);
                session.save(user25);
                session.save(user26);
                session.save(user1);
                session.save(user2);
                session.save(user3);
                session.save(user4);
                session.save(user5);
                session.save(user6);
                session.save(user7);
                session.save(user8);
                session.save(user9);
                session.save(user10);
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
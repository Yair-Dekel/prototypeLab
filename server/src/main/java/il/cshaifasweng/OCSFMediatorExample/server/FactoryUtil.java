package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.entities.Registered_user;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class FactoryUtil {

        private static final SessionFactory sessionFactory = buildSessionFactory();

        private static SessionFactory buildSessionFactory() {
            try {
                // Create a Configuration object and configure it
                Configuration configuration = new Configuration();

                configuration.addAnnotatedClass(Registered_user.class);
                configuration.addAnnotatedClass(Task.class);
                // Create a ServiceRegistry from hibernate configuration

                ServiceRegistry serviceRegistry = new
                        StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();
                return configuration.buildSessionFactory(serviceRegistry);

            } catch (Throwable ex) {
                // Handle initialization errors
                System.err.println("Initial SessionFactory creation failed: " + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }

        public static SessionFactory getSessionFactory() {
            return sessionFactory;
        }

}

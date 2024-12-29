package org.example.util;

import org.example.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class for creating and managing the Hibernate SessionFactory.
 */
public class SessionFactoryInstance {

    public static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure()
                    .addAnnotatedClass(Student.class)
                    .addAnnotatedClass(Teacher.class)
                    .addAnnotatedClass(Admin.class)
                    .addAnnotatedClass(Course.class)
                    .addAnnotatedClass(Enrollment.class)
                    .buildSessionFactory();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Shuts down the SessionFactory, releasing all resources.
     */
    public static void shutdown() {
        sessionFactory.close();
    }
}
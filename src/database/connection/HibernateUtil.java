package database.connection;

import database.seeders.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;
import java.util.List;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static List<Seeder> seeders = Arrays.asList(new UserSeeder(), new StateSeeder(), new CitySeeder(), new PeriodSeeder());

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.configure();
            sessionFactory = configuration.buildSessionFactory();
        }

        return sessionFactory;
    }

    public static void init() {
        getSessionFactory();
    }

    public static void runSeeders() {
        seeders.forEach(Seeder::run);
    }
}

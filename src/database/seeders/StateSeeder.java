package database.seeders;

import database.connection.HibernateUtil;
import database.models.address.State;
import database.service.Service;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class StateSeeder implements Seeder {

    public static void main(final String[] args) {
        new StateSeeder().run();
    }

    @Override
    public void run() {
        Service<State> stateService = new Service<State>(State.class);
        Long count = stateService.count();

        if (count.intValue() > 0) {
            return;
        }

        System.out.println("[Seeder] StateSeeder running.");
        try {
            List<String> querys = Files.readAllLines(new File("./resource/sqls/state_query.sql").toPath());
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            querys.forEach(query -> {
                session.createSQLQuery(query).executeUpdate();
            });
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[Seeder] StateSeeder error: " + e.getMessage());
        }
        System.out.println("[Seeder] StateSeeder runned.");
    }
}

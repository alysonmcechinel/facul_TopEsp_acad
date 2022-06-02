package database.service;

import database.connection.HibernateUtil;
import database.models.address.City;
import database.models.address.State;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CityService extends Service<City> {
    public CityService() {
        super(City.class);
    }

    public List<City> getCitiesFromState(State state) {
        if (state == null) {
            return null;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();

        Query query = session.createQuery(
                "select c from database.models.address.City as c " +
                        "inner join c.state as state " +
                        "where state.id=:stateId"
        );
        query.setParameter("stateId", state.getId());
        List<City> cities = (List<City>) query.getResultList();
        session.close();

        return cities;
    }
}

package database.models.user;

import database.connection.HibernateUtil;
import database.models.Model;
import database.service.Service;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(name = "users")
public class User extends Model<User> implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserTypeEnum type;

    public static User login(String nome, String senha) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Query query = session.createQuery("SELECT u FROM users u WHERE u.username = :username AND u.password = :password");
        query.setParameter("username", nome);
        query.setParameter("password", senha);
        User user = (User) query.getSingleResult();
        session.close();

        return user;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserTypeEnum getType() {
        return type;
    }

    public void setType(UserTypeEnum type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String[] getResult() {
        return new String[]{getName(), getUsername(), getType().name()};
    }

    @Override
    public List<User> filter(String value) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", value);
        map.put("username", value);
        return new Service<User>(User.class).findAllAndFilter(map);
    }
}

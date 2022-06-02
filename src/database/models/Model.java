package database.models;

import database.service.Service;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@MappedSuperclass
@Where(clause = "active = 1")
public abstract class Model<T extends Model<T>> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    public abstract String[] getResult();

    public abstract List<T> filter(String value);

    public T save() {
        Service<T> service = new Service<T>((Class<T>) this.getClass());
        return service.save((T) this);
    }

    public Boolean delete() {
        Service<T> service = new Service<T>((Class<T>) this.getClass());
        Boolean isDeleted = service.delete(getId());

        if (isDeleted) {
            setId(null);
        }

        return isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

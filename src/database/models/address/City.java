package database.models.address;


import database.models.Model;

import javax.persistence.*;
import java.util.List;

@Entity(name = "cities")
public class City extends Model<City> {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "state_id", foreignKey = @ForeignKey(name = "cities_state_id"))
    private State state;

    @Override
    public String[] getResult() {
        return new String[0];
    }

    @Override
    public List<City> filter(String value) {
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}

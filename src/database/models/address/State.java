package database.models.address;

import database.models.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.List;

@Entity(name = "states")
public class State extends Model<State> {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "uf", length = 2)
    private String uf;

    @Override
    public String[] getResult() {
        return new String[0];
    }

    @Override
    public List<State> filter(String value) {
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

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}

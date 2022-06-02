package database.models.modality;

import database.models.Model;
import database.models.period.Period;
import database.models.user.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "modalities")
public class Modality extends Model<Modality> {

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "modality_users")
    private List<User> teacher = new ArrayList<>();

    @Column(name = "description")
    private String description;

    @Column(name = "value")
    private BigDecimal value;

    @JoinTable(name = "modalities_periods",
            joinColumns = {@JoinColumn(name = "modality_id")},
            inverseJoinColumns = {@JoinColumn(name = "period_id")})
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Period> period = new ArrayList<>();

    public List<User> getTeacher() {
        return teacher;
    }

    public void setTeacher(List<User> teacher) {
        this.teacher = teacher;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public List<Period> getPeriod() {
        return period;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setPeriod(List<Period> period) {
        this.period = period;
    }

    @Override
    public String[] getResult() {
        return new String[0];
    }

    @Override
    public List<Modality> filter(String value) {
        return null;
    }
}

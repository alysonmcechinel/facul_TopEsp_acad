package database.models.modality;

import database.models.Model;
import database.models.address.Address;
import database.service.Service;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(name = "students")
public class Student extends Model<Student> {

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "modality_students",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "modality_id")})
    private List<Modality> modalities = new ArrayList<>();

    @Embedded
    private Address address;

    @Column
    private String telephone;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Modality> getModalities() {
        return modalities;
    }

    public void setModalities(List<Modality> modalities) {
        this.modalities = modalities;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getActiveText() {
        return getActive() ? "Sim" : "Não";
    }

    @Override
    public String[] getResult() {
        return new String[]{getName(), getTelephone(), getActiveText()};
    }

    @Override
    public List<Student> filter(String value) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", value);
        return new Service<Student>(Student.class).findAllAndFilter(map);
    }
}

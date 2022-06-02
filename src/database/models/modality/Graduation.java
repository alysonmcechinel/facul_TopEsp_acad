package database.models.modality;

import database.models.Model;

import javax.persistence.*;
import java.util.List;

@Entity(name = "graduations")
public class Graduation extends Model<Graduation> {

    @ManyToOne
    @JoinColumn(name = "modality_id", foreignKey = @ForeignKey(name = "graduation_modality_id"))
    private Modality modality;

    @Column(name = "description")
    private String description;

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String[] getResult() {
        return new String[0];
    }

    @Override
    public List<Graduation> filter(String value) {
        return null;
    }

}

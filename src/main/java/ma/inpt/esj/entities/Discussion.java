package ma.inpt.esj.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ma.inpt.esj.enums.DiscussionStatus;
import lombok.Builder;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class Discussion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    private Date date;
    private Long duree;
    private DiscussionStatus status;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Medecin> medecins;
    @ManyToOne(cascade = CascadeType.ALL)
    private Medecin responsable;
    @OneToOne(mappedBy = "discussion", cascade = CascadeType.ALL)
    private CompteRendu compteRendu;

    public void addMedecin(Medecin medecin) {
        this.medecins.add(medecin);
    }
}

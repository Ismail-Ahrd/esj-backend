package ma.inpt.esj.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Medecin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String cin;

    @Column(unique = true)
    private String inpe;

    @Column(unique = true)
    private String ppr;
  
    @Column(columnDefinition = "TEXT") // This annotation allows for a larger string
    private String about;
    private String sexe;

    private boolean estMedcinESJ;

    private boolean estGeneraliste;

    private String specialite;
        
    @OneToOne(cascade = CascadeType.ALL)
    private InfoUser infoUser;

    private String ROLE="MEDECIN";


    @Column(columnDefinition = "TEXT")
    private String aProposDeMoi;

    @ElementCollection
    @CollectionTable(name = "langues_parlees", joinColumns = @JoinColumn(name = "medecin_id"))
    @Column(name = "langue")
    private List<String> languesParlees;
    
    @ElementCollection
    @CollectionTable(name = "specialites_medicales", joinColumns = @JoinColumn(name = "medecin_id"))
    @Column(name = "specialite")
    private List<String> specialites;

    @ElementCollection
    @CollectionTable(name = "education", joinColumns = @JoinColumn(name = "medecin_id"))
    private List<Education> educations;

    @ElementCollection
    @CollectionTable(name = "experience", joinColumns = @JoinColumn(name = "medecin_id"))
    private List<Experience> experiences;

    @Column(name = "evaluation", columnDefinition = "INTEGER DEFAULT 0")
    private Integer evaluation;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Consultation> consultations;

}

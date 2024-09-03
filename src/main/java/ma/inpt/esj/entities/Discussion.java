package ma.inpt.esj.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

import ma.inpt.esj.enums.*;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class Discussion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String motif;
    private String prenomPatient;
    private String nomPatient;
    private String cinPatient;
    private String identifiantPatient;
    private String codeMassarPatient;
    
    @Enumerated(EnumType.STRING)
    private Sexe sexe;

    private int age;
    private String motifDeTeleExpertise;

    @ElementCollection
    private List<String> antecedentsMedicaux;

    private String antecedentsChirurgicaux;

    @ElementCollection
    private List<String> antecedentsFamiliaux;

    @ElementCollection
    private List<String> habitudes;
    
    private String descriptionDesHabitudes;
    private String descriptionEtatClinique;
    private String commentaireFichiers;

    @Enumerated(EnumType.STRING)
    private GenreDiscussion genre;

    @Enumerated(EnumType.STRING)
    private TypeDiscussion type;

    private Date date;
    private String heure;
    private Long duree;

    @Enumerated(EnumType.STRING)
    private DiscussionStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FichierAttache> fichiersAtaches = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Medecin> medecinsInvites = new ArrayList<>();

    @ElementCollection
    private List<String> specialitesDemandees = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "discussion")
    private List<Invitation> invitations = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Medecin> participants = new ArrayList<>();

    @ManyToOne
    private Medecin medcinResponsable;

    @ManyToOne
    private Medecin medcinConsulte;

    @OneToOne(mappedBy = "discussion", cascade = CascadeType.ALL, orphanRemoval = true)
    private CompteRendu compteRendu;

    public void addMedecin(Medecin medecin) {
        this.participants.add(medecin);
    }
}

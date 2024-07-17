package ma.inpt.esj.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

import ma.inpt.esj.enums.*;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class Discussion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String motif;
    private String prenomPatient;
    private String nomPatient;
    private Sexe sexe;
    private int age;
    private String motifDeTeleExpertise;
    private String antecedentsMedicaux;
    private String antecedentsChirurgicaux;
    private String habitudes;
    private String descriptionDesHabitudes;
    private String antecedentsFamiliaux;
    private String descriptionEtatClinique;
    private String commentaireFichiers;
    private GenreDiscussion genre;
    private TypeDiscussion type;
    private Date date;
    private LocalDateTime dateHeure;
    private Long duree;
    private DiscussionStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FichierAttache> fichiersAtaches = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Medecin> medecinsInvites = new ArrayList<>();

    @ElementCollection
    private List<String> specialitesDemandees = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Invitation> invitationsAcceptees = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Invitation> invitationsRejetees = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Medecin> participants = new ArrayList<>();

    @ManyToOne
    private Medecin medcinResponsable;

    @OneToOne(mappedBy = "discussion", cascade = CascadeType.ALL, orphanRemoval = true)
    private CompteRendu compteRendu;

    public void addMedecin(Medecin medecin) {
        this.participants.add(medecin);
    }
}

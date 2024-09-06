package ma.inpt.esj.entities;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ma.inpt.esj.enums.Sexe;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", length = 6, discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter 
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Jeune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "info_user_id",referencedColumnName = "id")
    private InfoUser infoUser;

    @Enumerated(EnumType.STRING)
    private Sexe sexe;

    private Date dateNaissance;

    private int age;

    private int identifiantPatient;

    private boolean scolarise;

    private String cin;

    private boolean favorite;

    private String adresse;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dossier_medical_id" ,nullable = true)
    private DossierMedical dossierMedial;

    private String ROLE="JEUNE";
    
    @OneToMany(mappedBy = "jeune")
    @JsonIgnore
    private List<Question> questionsList; 
    
    @OneToMany(mappedBy = "jeune")
    @JsonIgnore
    private List<LiveFeedback> feedbacks; 

//    @ElementCollection
//    @CollectionTable(name = "ap_medicaux", joinColumns = @JoinColumn(name = "jeune_id"))
//    @Column(name = "medicaux")
//    private List<String> medicaux;
//
//    @ElementCollection
//    @CollectionTable(name = "ap_chirurgicaux", joinColumns = @JoinColumn(name = "jeune_id"))
//    @Column(name = "chirurgicaux")
//    private List<String> chirurgicaux;
//
//    @ElementCollection
//    @CollectionTable(name = "ap_habitues", joinColumns = @JoinColumn(name = "jeune_id"))
//    @Column(name = "habitues")
//    private List<String> habitues;

//    @ElementCollection
//    @CollectionTable(name = "af_maladies", joinColumns = @JoinColumn(name = "jeune_id"))
//    @Column(name = "maladies_familiales")
//    private List<String> maladiesFamiliales;

//    @ElementCollection
//    @CollectionTable(name = "observation", joinColumns = @JoinColumn(name = "jeune_id"))
//    @Column(name = "observation")
//    private List<String> observation;
//
//    @ElementCollection
//    @CollectionTable(name = "consultation_op", joinColumns = @JoinColumn(name = "jeune_id"))
//    private List<ConsultationOp> consultation;
//    ALL MEDICAL INFORMATION ABOUT A JEUNE GOES INTO HIS DOSSIER_MEDICAL NOT HERE AS FIELDS
}

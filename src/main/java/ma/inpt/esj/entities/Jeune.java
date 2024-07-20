package ma.inpt.esj.entities;

import java.sql.Date;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dossier_medical_id" ,nullable = true)
    private DossierMedical dossierMedial;

    @ManyToOne
    @JoinColumn(name = "medecin_id" ,nullable = true)
    private Medecin medecin;

    private String ROLE="JEUNE";


    @OneToOne(mappedBy = "jeune" , cascade = CascadeType.ALL)
    private AntecedentFamilial antecedentFamilial;

    @OneToOne(mappedBy = "jeune" , cascade = CascadeType.ALL)
    private AntecedentPersonnel antecedentPersonnel;
}

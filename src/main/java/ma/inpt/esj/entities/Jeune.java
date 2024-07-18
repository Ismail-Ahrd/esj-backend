package ma.inpt.esj.entities;

import java.sql.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ma.inpt.esj.enums.Sexe;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter 
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Jeune{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Sexe sexe;

    private Date dateNaissance;

    private int age;

    private int identifiantPatient;

    private boolean scolarise;

    private String cin;

    @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "dossier_medical_id")

    private DossierMedical dossierMedial;

    @OneToOne(cascade = CascadeType.ALL)
            @JoinColumn(name = "info_user_id")

    private InfoUser infoUser;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    private Medecin medecin;
    
    public boolean getScolarise() {
        return scolarise;
    }
}

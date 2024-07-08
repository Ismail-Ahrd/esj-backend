package ma.inpt.esj.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ma.inpt.esj.enums.Sexe;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class Jeune extends InfoUser{
    private Sexe sexe;
    private Date dateNaissance;
    private int age;
    private int identifiantPatient;
    private boolean scolarise;
    private String cin;
}

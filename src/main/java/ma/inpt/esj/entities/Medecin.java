package ma.inpt.esj.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class Medecin extends InfoUser{
    private String cin;
    private String inpe;
    private String ppr;
    private boolean estMedcinESJ;
    private boolean estGeneraliste;
    private String specialite;
}

package ma.inpt.esj.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ma.inpt.esj.enums.NiveauEtudes;
import ma.inpt.esj.enums.Situation;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class JeuneNonScolarise extends Jeune{
    private NiveauEtudes derniereNiveauEtudes;
    private Situation situationActuelle;
}

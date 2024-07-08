package ma.inpt.esj.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ma.inpt.esj.enums.NiveauEtudes;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class JeuneScolarise extends Jeune{
    private NiveauEtudes niveauEtudeActuel;
    private String cne;
    private String codeMassare;

}

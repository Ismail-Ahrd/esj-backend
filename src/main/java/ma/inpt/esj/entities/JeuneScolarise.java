package ma.inpt.esj.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ma.inpt.esj.enums.NiveauEtudes;

@Entity
@DiscriminatorValue("SC")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class JeuneScolarise extends Jeune{
    @Enumerated(EnumType.STRING)
    private NiveauEtudes niveauEtudeActuel;
    @Column(unique = true)
    private String cne;
    @Column(unique = true)
    private String codeMassare;

}

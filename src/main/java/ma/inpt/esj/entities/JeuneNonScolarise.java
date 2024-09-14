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
@DiscriminatorValue("NONSC")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class JeuneNonScolarise extends Jeune{
    @Enumerated(EnumType.STRING)
    private NiveauEtudes derniereNiveauEtudes;
    private boolean enActivite;
    @Column(unique = true)
    private String cne;
    @Column(unique = true)
    private String codeMassare;
}

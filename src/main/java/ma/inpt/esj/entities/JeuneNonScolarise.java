package ma.inpt.esj.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ma.inpt.esj.enums.NiveauEtudes;
import ma.inpt.esj.enums.Situation;

@Entity
@DiscriminatorValue("NONSC")
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class JeuneNonScolarise extends Jeune{
    @Enumerated(EnumType.STRING)
    private NiveauEtudes derniereNiveauEtudes;
    private boolean enActivite;
}

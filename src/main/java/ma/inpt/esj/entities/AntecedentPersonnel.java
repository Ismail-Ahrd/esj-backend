package ma.inpt.esj.entities;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AntecedentPersonnel {
    private String type;
    private String specification;
    private String specificationAutre;
    private int nombreAnnee ;
}


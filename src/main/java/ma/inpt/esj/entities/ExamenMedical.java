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
public class ExamenMedical {

    private String typeExamen; // Biologique - Radiologique
    private String specificationExamen;
    private String autreSpecification;

}

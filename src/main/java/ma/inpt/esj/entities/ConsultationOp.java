package ma.inpt.esj.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class ConsultationOp {
    private String date_consultation;

    private String motif_consultation;

    private String diagnostic;

    private String traitement;

    private String recommandation;
}


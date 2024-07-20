package ma.inpt.esj.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationChirurgicale {
    private String typeOperation;
    private Integer anneeOperation;
}

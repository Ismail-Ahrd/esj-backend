package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EducationDTO {
    private String annee;
    private String diplome;
    private String institut;
}

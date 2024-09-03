package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EducationDTO {
    private Long id;
    private String annee;
    private String diplome;
    private String institut;

    public EducationDTO(String annee, String diplome, String institut) {
        this.annee = annee;
        this.diplome = diplome;
        this.institut = institut;
    }
}

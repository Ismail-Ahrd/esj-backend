package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDTO {
    private Long id;
    private String annee;
    private String hopital;
    private String poste;

    public ExperienceDTO(String annee, String hopital, String poste) {
        this.annee = annee;
        this.hopital = hopital;
        this.poste = poste;
    }
}

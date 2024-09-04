package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.inpt.esj.entities.InfoUser;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedecinRequestDTO {
    private String cin;
    private String inpe;
    private boolean estMedcinESJ;
    private boolean estGeneraliste;
    private String specialite;
    private InfoUser infoUser;
}

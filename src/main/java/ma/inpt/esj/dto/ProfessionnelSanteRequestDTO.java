package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.inpt.esj.entities.InfoUser;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionnelSanteRequestDTO {

    private String cin;
    private String inpe;
    InfoUser infoUser;
}

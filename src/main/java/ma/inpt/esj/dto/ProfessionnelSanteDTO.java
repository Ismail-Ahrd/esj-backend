package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionnelSanteDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String mail;
    private String numTel;
    private String cin;
    private String inpe;

}

package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedecinResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
        private String about;
        private String sexe;

    private String mail;
    private String cin;
    private String inpe;
    private String ppr;
    private Boolean estMedcinESJ;
    private Boolean estGeneraliste;
    private String specialite;

    public MedecinResponseDTO(String s) {
    }
}

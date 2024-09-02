package ma.inpt.esj.dto;

import java.util.List;

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
    private String aProposDeMoi;
    private String sexe;
    private String mail;
    private String cin;
    private String inpe;
    private String ppr;
    private Boolean estMedcinESJ;
    private Boolean estGeneraliste;
    private String specialite;
    private List<EducationDTO> medicalStudies;

    public MedecinResponseDTO(String s) {
    }
}

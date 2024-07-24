package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
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
            private String password;

private String linkedin;
        private String twitter;
        private List<EducationDTO> education;
    private List<ExperienceDTO> experience;

    // Inner classes for Education and Experience DTOs
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EducationDTO {
        private String year;
        private String diploma;
        private String institut;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExperienceDTO {
        private String year;
        private String position;
        private String hospital;
    }
    public MedecinResponseDTO(String s) {
    }
}

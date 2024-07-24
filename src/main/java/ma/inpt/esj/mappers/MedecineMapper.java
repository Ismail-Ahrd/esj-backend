package ma.inpt.esj.mappers;

import ma.inpt.esj.dto.MedecinResponseDTO;
import ma.inpt.esj.entities.Medecin;
import org.springframework.stereotype.Service;

@Service
public class MedecineMapper {

    public MedecinResponseDTO fromMedcine(Medecin medecin) {
        MedecinResponseDTO medecinResponseDTO = new MedecinResponseDTO();

        medecinResponseDTO.setId(medecin.getId());
        medecinResponseDTO.setNom(medecin.getInfoUser().getNom());
        medecinResponseDTO.setPrenom(medecin.getInfoUser().getPrenom());
        medecinResponseDTO.setMail(medecin.getInfoUser().getMail());
        medecinResponseDTO.setCin(medecin.getCin());
        medecinResponseDTO.setInpe(medecin.getInpe());
        medecinResponseDTO.setPpr(medecin.getPpr());
        medecinResponseDTO.setAbout(medecin.getAbout());
        medecinResponseDTO.setSexe(medecin.getSexe());
        medecinResponseDTO.setLinkedin(medecin.getLinkedin());
        medecinResponseDTO.setTwitter(medecin.getTwitter());
        medecinResponseDTO.setPassword(medecin.getInfoUser().getMotDePasse());
        medecinResponseDTO.setEstMedcinESJ(medecin.isEstMedcinESJ());
        medecinResponseDTO.setEstGeneraliste(medecin.isEstGeneraliste());
        medecinResponseDTO.setSpecialite(medecin.getSpecialite());

        // Map education
        if (medecin.getEducation() != null) {
            medecinResponseDTO.setEducation(medecin.getEducation().stream()
                .map(e -> new MedecinResponseDTO.EducationDTO(
                    e.getYear(),
                    e.getDiploma(),
                    e.getInstitut()
                ))
                .toList());
        }

        // Map experience
        if (medecin.getExperience() != null) {
            medecinResponseDTO.setExperience(medecin.getExperience().stream()
                .map(e -> new MedecinResponseDTO.ExperienceDTO(
                    e.getYear(),
                    e.getPosition(),
                    e.getHospital()
                ))
                .toList());
        }

        return medecinResponseDTO;
    }
}

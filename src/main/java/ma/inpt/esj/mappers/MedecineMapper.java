package ma.inpt.esj.mappers;

import ma.inpt.esj.dto.EducationDTO;
import ma.inpt.esj.dto.ExperienceDTO;
import ma.inpt.esj.dto.MedecinRequestDTO;
import ma.inpt.esj.dto.MedecinResponseDTO;
import ma.inpt.esj.entities.Medecin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class MedecineMapper {
    public MedecinResponseDTO fromMedcine(Medecin medecin){
        MedecinResponseDTO medecinResponseDTO=new MedecinResponseDTO();
        medecinResponseDTO.setId(medecin.getId());
        medecinResponseDTO.setNom(medecin.getInfoUser().getNom());
        medecinResponseDTO.setImage_url(medecin.getInfoUser().getImage_url());
        medecinResponseDTO.setPrenom(medecin.getInfoUser().getPrenom());
        medecinResponseDTO.setMail(medecin.getInfoUser().getMail());
        medecinResponseDTO.setCin(medecin.getCin());
        medecinResponseDTO.setInpe(medecin.getInpe());
        medecinResponseDTO.setAbout(medecin.getAbout());
        medecinResponseDTO.setLinkedin(medecin.getLinkedin());
        medecinResponseDTO.setPpr(medecin.getPpr());
        medecinResponseDTO.setSexe(medecin.getSexe());
        medecinResponseDTO.setEstMedcinESJ(medecin.isEstMedcinESJ());
        medecinResponseDTO.setEstGeneraliste(medecin.isEstGeneraliste());
        medecinResponseDTO.setSpecialite(medecin.getSpecialite());

        List<EducationDTO> educationDTOs = medecin.getEducations().stream()
                .map(e -> new EducationDTO(e.getId(), e.getAnnee(), e.getDiplome(), e.getInstitut()))
                .collect(Collectors.toList());
        medecinResponseDTO.setMedicalStudies(educationDTOs);

        List<ExperienceDTO> experienceDTOs = medecin.getExperiences().stream()
                .map(e -> new ExperienceDTO(e.getAnnee(), e.getHopital(), e.getPoste()))
                .collect(Collectors.toList());
        medecinResponseDTO.setMedicalExperience(experienceDTOs);
        
        return medecinResponseDTO;
    }

    public Medecin fromMedecinDTO(MedecinRequestDTO dto){
        Medecin medecin = new Medecin();
        medecin.setCin(dto.getCin());
        medecin.setInpe(dto.getInpe());
        medecin.setEstMedcinESJ(dto.isEstMedcinESJ());
        medecin.setEstGeneraliste(dto.isEstGeneraliste());
        medecin.setSpecialite(dto.getSpecialite());
        medecin.setInfoUser(dto.getInfoUser());
        return medecin;
    }
}

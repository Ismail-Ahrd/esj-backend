package ma.inpt.esj.mappers;

import ma.inpt.esj.dto.EducationDTO;
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
        medecinResponseDTO.setPrenom(medecin.getInfoUser().getPrenom());
        medecinResponseDTO.setMail(medecin.getInfoUser().getMail());
        medecinResponseDTO.setCin(medecin.getCin());
        medecinResponseDTO.setInpe(medecin.getInpe());
        medecinResponseDTO.setAProposDeMoi(medecin.getAProposDeMoi());
        medecinResponseDTO.setAbout(medecin.getAbout());
        medecinResponseDTO.setPpr(medecin.getPpr());
        medecinResponseDTO.setSexe(medecin.getSexe());
        medecinResponseDTO.setEstMedcinESJ(medecin.isEstMedcinESJ());
        medecinResponseDTO.setEstGeneraliste(medecin.isEstGeneraliste());
        medecinResponseDTO.setSpecialite(medecin.getSpecialite());

        List<EducationDTO> educationDTOs = medecin.getEducations().stream()
                .map(e -> new EducationDTO(e.getAnnee(), e.getDiplome(), e.getInstitut()))
                .collect(Collectors.toList());
        medecinResponseDTO.setMedicalStudies(educationDTOs);
        
        return medecinResponseDTO;
    }
}

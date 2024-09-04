package ma.inpt.esj.mappers;


import ma.inpt.esj.dto.ProfessionnelSanteDTO;
import ma.inpt.esj.dto.ProfessionnelSanteRequestDTO;
import ma.inpt.esj.entities.ProfessionnelSante;
import org.springframework.stereotype.Service;

@Service
public class ProfessionnelMapper {
    public ProfessionnelSanteDTO fromProfessionnel(ProfessionnelSante professionnel){
        ProfessionnelSanteDTO professionnelSanteDTO=new ProfessionnelSanteDTO();
        professionnelSanteDTO.setId(professionnel.getId());
        professionnelSanteDTO.setNom(professionnel.getInfoUser().getNom());
        professionnelSanteDTO.setPrenom(professionnel.getInfoUser().getPrenom());
        professionnelSanteDTO.setMail(professionnel.getInfoUser().getMail());
        professionnelSanteDTO.setNumTel(professionnel.getInfoUser().getNumTel());
        professionnelSanteDTO.setCin(professionnel.getCin());
        professionnelSanteDTO.setInpe(professionnel.getInpe());

        return professionnelSanteDTO;
    }

    public ProfessionnelSante fromProfessionnelDTO(ProfessionnelSanteRequestDTO dto){
        ProfessionnelSante professionnelSante=new ProfessionnelSante();
        professionnelSante.setCin(dto.getCin());
        professionnelSante.setInpe(dto.getInpe());
        professionnelSante.setInfoUser(dto.getInfoUser());
        return professionnelSante;
    }
}

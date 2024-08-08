package ma.inpt.esj.mappers;

import org.springframework.stereotype.Service;

import ma.inpt.esj.dto.AdministrateurDTO;
import ma.inpt.esj.entities.Administrateur;

@Service
public class AdministrateurMapper {
    public AdministrateurDTO adminToAdminDTO(Administrateur admin){
        AdministrateurDTO adminResponseDTO=new AdministrateurDTO();
        adminResponseDTO.setId(admin.getId());
        adminResponseDTO.setInfoUser(admin.getInfoUser());
        return adminResponseDTO;
    }
}

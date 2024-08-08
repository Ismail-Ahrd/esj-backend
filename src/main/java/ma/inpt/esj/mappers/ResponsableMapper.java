package ma.inpt.esj.mappers;

import org.springframework.stereotype.Service;

import ma.inpt.esj.dto.ResponsableDTO;
import ma.inpt.esj.entities.Responsable;
@Service

public class ResponsableMapper {

        public ResponsableDTO fromResponsable(Responsable reponsable){
            ResponsableDTO medecinResponseDTO=new ResponsableDTO(reponsable);
            return medecinResponseDTO;
        }
}

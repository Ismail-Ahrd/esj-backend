package ma.inpt.esj.services;



import ma.inpt.esj.dto.ProfessionnelSanteDTO;
import ma.inpt.esj.entities.ProfessionnelSante;
import ma.inpt.esj.exception.ProfessionnelException;
import ma.inpt.esj.exception.ProfessionnelNotFoundException;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Map;

public interface ProfessionnelService {
    ProfessionnelSanteDTO saveProfessionnel(ProfessionnelSante professionnel) throws ProfessionnelException;

    ProfessionnelSanteDTO getProfessionnelById(Long id) throws ProfessionnelNotFoundException;

    ProfessionnelSanteDTO updateProfessionnel(Long id, Map<String, Object> updates) throws ProfessionnelNotFoundException;

    void deleteProfessionnel(Long id) throws ProfessionnelNotFoundException, ProfessionnelException;
    List<ProfessionnelSanteDTO> getAllProfessionnels();

    Map<String, String> confirmAuthentification(Long id,String password) throws BadRequestException;

}

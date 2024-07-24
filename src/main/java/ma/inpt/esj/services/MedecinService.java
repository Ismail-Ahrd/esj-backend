package ma.inpt.esj.services;


import ma.inpt.esj.dto.MedecinResponseDTO;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.exception.MedecinException;
import ma.inpt.esj.exception.MedecinNotFoundException;
import org.apache.coyote.BadRequestException;

import java.util.Map;
import java.util.List;

public interface MedecinService {
    MedecinResponseDTO saveMedecin(Medecin medecin) throws MedecinException;

    MedecinResponseDTO getMedecinById(Long id) throws MedecinNotFoundException;

    MedecinResponseDTO updateMedecinPartial(Long id, Map<String, Object> updates) throws MedecinNotFoundException;

    void deleteMedecin(Long id) throws MedecinNotFoundException, MedecinException;
    List<MedecinResponseDTO> getAllMedecins();

    Map<String, String> confirmAuthentification(Long id,String password) throws BadRequestException;

}

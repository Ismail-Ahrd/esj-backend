package ma.inpt.esj.services;




import ma.inpt.esj.dto.JeuneDto;
import ma.inpt.esj.entities.AntecedentFamilial;
import ma.inpt.esj.entities.AntecedentPersonnel;
import ma.inpt.esj.entities.Consultation;
import ma.inpt.esj.dto.ConsultationDTO;
import ma.inpt.esj.entities.Jeune;
import ma.inpt.esj.exception.EmailNonValideException;
import ma.inpt.esj.exception.JeuneNotFoundException;
import ma.inpt.esj.exception.PhoneNonValideException;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Map;


public interface JeuneService {
    JeuneDto saveJeune(Jeune jeune) throws EmailNonValideException, PhoneNonValideException;
    public List<Jeune> getAllJeunes();
    public List<Jeune> getFiltredJeunes(Long id, String nom, String prenom);

    public String sendJeuneToKafka(Jeune jeune);
    public Jeune saveOrUpdate(Jeune jeune);
    Jeune addConsultationToJeune(Long jeuneId, Consultation consultation);
    Jeune addConsultationDTOToJeune(Long id, ConsultationDTO consultationDTO);
    Jeune addAntecedentPersonnelToJeune(Long id, AntecedentPersonnel antecedentPersonnel);
    Jeune addAntecedentFamilialToJeune(Long id, AntecedentFamilial antecedentFamilial);
    Jeune updateConsultationDTOJeune(Long id, ConsultationDTO consultationDTO, Long idConsultation);

    Map<String, List<String>> getAntecedentFamilByJeuneId(Long jeuneId) throws JeuneNotFoundException;
    Map<String,Object> getAntecedentPersonelByJeuneId(Long jeuneId) throws JeuneNotFoundException;

    Object getJeuneById(Long id) throws JeuneNotFoundException;

     Jeune getJeuneById2(Long id) throws JeuneNotFoundException;

    public void deleteJeune(Long id);

    JeuneDto updateJeunePartial(Long id, Map<String, Object> updates) throws JeuneNotFoundException;

    Map<String, String> confirmAuthentification( Long id,String password) throws BadRequestException;

}
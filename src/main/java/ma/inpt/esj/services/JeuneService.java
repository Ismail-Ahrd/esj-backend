package ma.inpt.esj.services;




import ma.inpt.esj.dto.JeuneDto;
import ma.inpt.esj.entities.AntecedentFamilial;
import ma.inpt.esj.entities.AntecedentPersonnel;
import ma.inpt.esj.entities.Consultation;
import ma.inpt.esj.dto.ConsultationDTO;
import ma.inpt.esj.entities.Jeune;
import ma.inpt.esj.exception.EmailNonValideException;
import ma.inpt.esj.exception.JeuneException;
import ma.inpt.esj.exception.JeuneNotFoundException;
import ma.inpt.esj.exception.PhoneNonValideException;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Map;


public interface JeuneService {
    JeuneDto saveJeune(Jeune jeune) throws EmailNonValideException, PhoneNonValideException;

    Jeune addConsultationToJeune(Long jeuneId, Consultation consultation);
    Jeune addConsultationDTOToJeune(Long id, ConsultationDTO consultationDTO);
    Jeune addAntecedentPersonnelToJeune(Long id, AntecedentPersonnel antecedentPersonnel);
    Jeune addAntecedentFamilialToJeune(Long id, AntecedentFamilial antecedentFamilial);


    Object getJeuneById(Long id) throws JeuneNotFoundException;

    public void deleteJeune(Long id);

    JeuneDto updateJeunePartial(Long id, Map<String, Object> updates) throws JeuneNotFoundException;

    public List<Jeune> getAllJeunesOrderByPrenom();
    public List<Jeune> getAllJeunesOrderByNom();
    public List<Jeune> getAllJeunesOrderByAgeDesc();
    public List<Jeune> getAllJeunesOrderByAgeAsc();

    public List<Jeune> getAllJeunesByNom(String nom);
    public List<Jeune> getAllJeunesBySexe(String sexe);

    Map<String, String> confirmAuthentification( Long id,String password) throws BadRequestException;

}
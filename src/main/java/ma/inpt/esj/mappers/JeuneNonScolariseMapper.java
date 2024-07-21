package ma.inpt.esj.mappers;



import ma.inpt.esj.dto.JeuneNonScolariseDto;
import ma.inpt.esj.entities.InfoUser;
import ma.inpt.esj.entities.JeuneNonScolarise;
import org.springframework.stereotype.Service;

@Service
public class JeuneNonScolariseMapper {

    public JeuneNonScolariseDto toDtoJeuneNS(JeuneNonScolarise jeuneNonScolarise) {
        JeuneNonScolariseDto jeuneNonScolariseDto = new JeuneNonScolariseDto();
        InfoUser infoUser = jeuneNonScolarise.getInfoUser();

        jeuneNonScolariseDto.setId(jeuneNonScolarise.getId());
        jeuneNonScolariseDto.setPrenom(infoUser.getPrenom());
        jeuneNonScolariseDto.setNom(infoUser.getNom());
        jeuneNonScolariseDto.setMail(infoUser.getMail());
        jeuneNonScolariseDto.setNumTele(infoUser.getNumTel());
        jeuneNonScolariseDto.setSexe(jeuneNonScolarise.getSexe());
        jeuneNonScolariseDto.setDateNaissance(jeuneNonScolarise.getDateNaissance());
        jeuneNonScolariseDto.setAge(jeuneNonScolarise.getAge());
        jeuneNonScolariseDto.setIdentifiantPatient(jeuneNonScolarise.getIdentifiantPatient());
        jeuneNonScolariseDto.setCin(jeuneNonScolarise.getCin());
        jeuneNonScolariseDto.setConfirmed(jeuneNonScolarise.getInfoUser().isConfirmed());
        jeuneNonScolariseDto.setFirstAuth(jeuneNonScolarise.getInfoUser().isFirstAuth());
        jeuneNonScolariseDto.setROLE(jeuneNonScolarise.getROLE());
        jeuneNonScolariseDto.setDernierNiveauEtudes(jeuneNonScolarise.getDerniereNiveauEtudes());
        jeuneNonScolariseDto.setEnActivite(jeuneNonScolarise.isEnActivite());

        return jeuneNonScolariseDto;
    }
}


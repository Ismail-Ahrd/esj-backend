package ma.inpt.esj.mappers;




import ma.inpt.esj.dto.JeuneDto;
import ma.inpt.esj.entities.Jeune;
import org.springframework.stereotype.Service;

@Service
public class JeuneMapper {
    public static JeuneDto toDtoJeune(Jeune jeune) {
        JeuneDto dto = new JeuneDto();
        dto.setId(jeune.getId());
        dto.setNom(jeune.getInfoUser().getNom());
        dto.setPrenom(jeune.getInfoUser().getPrenom());
        dto.setMail(jeune.getInfoUser().getMail());
        dto.setNumTele(jeune.getInfoUser().getNumTel());
        dto.setSexe(jeune.getSexe());
        dto.setDateNaissance(jeune.getDateNaissance());
        dto.setAge(jeune.getAge());
        dto.setIdentifiantPatient(jeune.getIdentifiantPatient());
        dto.setScolarise(jeune.isScolarise());
        dto.setCin(jeune.getCin());
        dto.setIsConfirmed(jeune.getInfoUser().isConfirmed());
        dto.setIsFirstAuth(jeune.getInfoUser().isFirstAuth());
        dto.setRole(jeune.getROLE());
        return dto;
    }
}

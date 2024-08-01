package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsultationDTO {
    public Date date;
    public String motif;
    public AntecedentPersonnelDTO antecedentPersonnel;
    public AntecedentFamilialDTO antecedentFamilial;
    public ExamenMedicalDTO examenMedical;
    public String historiqueClinique;
    public String examenClinique;
    public String Diagnostic; // "oui" - "non" - "correspondance" - "tele-expertise"
    public String Ordonnance;
    public Long jeuneId;
    public Long medecinId;
    public Long dossierMedicalId;
}


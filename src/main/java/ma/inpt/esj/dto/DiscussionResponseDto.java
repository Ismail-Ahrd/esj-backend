package ma.inpt.esj.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.inpt.esj.entities.FichierAttache;
import ma.inpt.esj.enums.DiscussionStatus;
import ma.inpt.esj.enums.GenreDiscussion;
import ma.inpt.esj.enums.Sexe;
import ma.inpt.esj.enums.TypeDiscussion;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class DiscussionResponseDto {
    private Long id;
    private String titre;
    private String motif;
    private String prenomPatient;
    private String nomPatient;
    private Sexe sexe;
    private int age;
    private String motifDeTeleExpertise;
    private String antecedentsMedicaux;
    private String antecedentsChirurgicaux;
    private String habitudes;
    private String descriptionDesHabitudes;
    private String antecedentsFamiliaux;
    private String descriptionEtatClinique;
    private String commentaireFichiers;
    private GenreDiscussion genre;
    private TypeDiscussion type;
    private Date date;
    private String heure;
    private Long duree;
    private DiscussionStatus status;
    private List<FichierAttache> fichiersAtaches = new ArrayList<>();
    private List<Long>  medecinsInvitesIds = new ArrayList<>();
    private List<InvitationDto> invitations = new ArrayList<>();
    private List<MedecinResponseDTO> participants = new ArrayList<>();
    private List<String> specialitesDemandees = new ArrayList<>();
    private MedecinResponseDTO medcinResponsable;
    private CompteRenduDto compteRendu;
}

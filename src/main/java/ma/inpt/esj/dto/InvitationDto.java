package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.inpt.esj.enums.InvitationStatus;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class InvitationDto {
    private Long id;
    private InvitationStatus status;
    private MedecinResponseDTO medecinInvite;
    private Long discussionId;
    private DiscussionResponseDto discussion;
}

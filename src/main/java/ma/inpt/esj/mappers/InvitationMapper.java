package ma.inpt.esj.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.inpt.esj.dto.DiscussionResponseDto;
import ma.inpt.esj.dto.InvitationDto;
import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.entities.Invitation;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.repositories.DiscussionRepository;
import ma.inpt.esj.repositories.MedecinRepository;

@Service
@RequiredArgsConstructor
public class InvitationMapper {
    private final MedecinRepository medecinRepository;
    private final MedecineMapper medecineMapper;
    private final DiscussionRepository discussionRepository;
    
    public Invitation fromInvitationDto(InvitationDto invitationDto) {
        Invitation invitation = new Invitation();
        BeanUtils.copyProperties(invitationDto, invitation);
        Medecin medecin = medecinRepository.findById(invitationDto.getMedecinInvite().getId()).orElse(null);
        invitation.setMedecinInvite(medecin);
        Discussion discussion = discussionRepository.findById(invitationDto.getDiscussionId()).orElse(null);
        invitation.setDiscussion(discussion);
        return invitation;
    }

    public InvitationDto fromInvitation(Invitation invitation) {
        InvitationDto invitationDto = new InvitationDto();
        BeanUtils.copyProperties(invitation, invitationDto);
        invitationDto.setMedecinInvite(medecineMapper.fromMedcine(invitation.getMedecinInvite()));
        invitationDto.setDiscussionId(invitation.getDiscussion().getId());
        DiscussionResponseDto discussionResponseDto = new DiscussionResponseDto();
        BeanUtils.copyProperties(invitation.getDiscussion(), discussionResponseDto);
        discussionResponseDto.setInvitations(null);
        discussionResponseDto.setParticipants(null);
        discussionResponseDto.setMedecinsInvitesIds(null);
        discussionResponseDto.setFichiersAtaches(null);
        discussionResponseDto.setMedcinResponsable(medecineMapper.fromMedcine(invitation.getDiscussion().getMedcinResponsable()));
        invitationDto.setDiscussion(discussionResponseDto);
        return invitationDto;
    }

}

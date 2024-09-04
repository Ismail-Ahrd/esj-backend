package ma.inpt.esj.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.inpt.esj.dto.DiscussionRequestDto;
import ma.inpt.esj.dto.DiscussionResponseDto;
import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.repositories.MedecinRepository;

@Service
@RequiredArgsConstructor
public class DiscussionMapper {
    private final MedecinRepository medecinRepository;
    private final InvitationMapper invitationMapper;
    private final MedecineMapper medecineMapper;
    private final CompteRenduMapper compteRenduMapper;

    public Discussion fromDiscussionRequestDtoToDiscussion(DiscussionRequestDto discussionRequestDto){
        Discussion discussion = new Discussion();
        BeanUtils.copyProperties(discussionRequestDto, discussion);
        Medecin medecinResponsable = medecinRepository.findById(discussionRequestDto.getMedcinResponsableId()).get();
        discussion.setMedcinResponsable(medecinResponsable);
        Medecin medecinConsulte = medecinRepository.findById(discussionRequestDto.getMedcinConsulteId()).get();
        discussion.setMedcinConsulte(medecinConsulte);
        discussionRequestDto.getMedecinsInvitesIds().forEach(id -> {
            Medecin medecin = medecinRepository.findById(id).get();
            discussion.getMedecinsInvites().add(medecin);
        });
        return discussion;
    }
    
    public DiscussionRequestDto fromDiscussionToDiscussionRequestDto(Discussion discussion) {
        DiscussionRequestDto discussionRequestDto = new DiscussionRequestDto();
        BeanUtils.copyProperties(discussion, discussionRequestDto);
        return discussionRequestDto;
    }

    public DiscussionResponseDto fromDiscussionToDiscussionResponseDto(Discussion discussion) {
        DiscussionResponseDto discussionResponseDto = new DiscussionResponseDto();
        BeanUtils.copyProperties(discussion, discussionResponseDto);

        discussionResponseDto.setMedcinResponsable(medecineMapper.fromMedcine(discussion.getMedcinResponsable()));
        discussionResponseDto.setMedcinConsulte(medecineMapper.fromMedcine(discussion.getMedcinConsulte()));
        
        discussionResponseDto.setCompteRendu(compteRenduMapper.fromCompteRendu(discussion.getCompteRendu()));
 
        discussion.getMedecinsInvites().forEach(m -> {
            discussionResponseDto.getMedecinsInvitesIds().add(m.getId());
        });
        discussion.getInvitations().forEach(i -> {
            discussionResponseDto.getInvitations().add(invitationMapper.fromInvitation(i));
        });
        discussion.getParticipants().forEach(m -> {
            discussionResponseDto.getParticipants().add(medecineMapper.fromMedcine(m));
        });
        return discussionResponseDto;
    }

}

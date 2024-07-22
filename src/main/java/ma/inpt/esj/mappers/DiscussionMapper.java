package ma.inpt.esj.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.inpt.esj.dto.DiscussionRequestDto;
import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.repositories.MedecinRepository;

@Service
@RequiredArgsConstructor
public class DiscussionMapper {
    private final MedecinRepository medecinRepository;

    public Discussion fromDiscussionRequestDtoToDiscussion(DiscussionRequestDto discussionRequestDto){
        Discussion discussion = new Discussion();
        BeanUtils.copyProperties(discussionRequestDto, discussion);
        Medecin medecinResponsable = medecinRepository.findById(discussionRequestDto.getMedcinResponsableId()).get();
        discussion.setMedcinResponsable(medecinResponsable);
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

}

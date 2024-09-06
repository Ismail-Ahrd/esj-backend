package ma.inpt.esj.services;

import ma.inpt.esj.dto.DiscussionRequestDto;
import ma.inpt.esj.dto.DiscussionResponseDto;
import ma.inpt.esj.dto.PageResponseDto;
import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.enums.DiscussionStatus;
import ma.inpt.esj.exception.DiscussionException;
import ma.inpt.esj.exception.DiscussionNotFoundException;
import ma.inpt.esj.exception.MedecinNotFoundException;
import java.util.List;

public interface DiscussionService {
    List<DiscussionResponseDto>  getAllDiscussions() throws DiscussionException;
    DiscussionResponseDto createDiscussion(DiscussionRequestDto discussionRequestDto, Long organizerId) throws DiscussionException;
    /* 
    List<Discussion> getDiscussionsByMedecinSpecialite(Long medecinId) throws MedecinNotFoundException;
    List<Discussion> getDiscussionByMedecinResponsable(Long medcinId) throws MedecinNotFoundException;
    List<Discussion> getByParticipantId(Long medecinId) throws MedecinNotFoundException;
    List<Discussion> getFinishedDiscussionsByParticipantId(Long medecinId) throws MedecinNotFoundException; 
    */
    List<DiscussionResponseDto>  getOuverteDiscussions() throws DiscussionException;
    PageResponseDto<DiscussionResponseDto> getMyDiscussions(
        Long organizerId, String keyword, DiscussionStatus status, boolean isParticipant, int page, int size
    ) throws DiscussionException;
    Discussion getDiscussion(Long id) throws DiscussionNotFoundException;
    DiscussionResponseDto getDiscussionResponseDto(Long id) throws DiscussionNotFoundException; 
    DiscussionResponseDto startDiscussion(Long id, Long userId) throws DiscussionNotFoundException, DiscussionException;
    DiscussionResponseDto endDiscussion(Long id, Long userId) throws DiscussionNotFoundException, DiscussionException;;
    Discussion joinDiscussion(Long id, Long medecinId) throws DiscussionNotFoundException, MedecinNotFoundException, DiscussionException;
    List<DiscussionResponseDto> getDiscussionsInMonth(Long organizerId, int year, int month) throws DiscussionException;
}

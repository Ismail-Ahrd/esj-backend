package ma.inpt.esj.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.enums.DiscussionStatus;
import ma.inpt.esj.enums.TypeDiscussion;
import ma.inpt.esj.repositories.DiscussionRepository;
import ma.inpt.esj.utils.JwtUtil;
import ma.inpt.esj.utils.SignalMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscussionVideoChatServiceImpl implements DiscussionVideoChatService {
    private final SimpMessagingTemplate messagingTemplate;
    private final DiscussionRepository discussionRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public void sendEvent(String eventName, SignalMessage payload, SimpMessageHeaderAccessor headerAccessor) {
        log.info("Received "+eventName+" event");
        Jwt jwt = (Jwt) headerAccessor.getSessionAttributes().get("jwt");
        Long userId = jwtUtil.getUserIdFromJwt(jwt);
        Discussion discussion = discussionRepository.findById(payload.getDiscussionId()).orElse(null);

        List<Long> participantsIds = null;
        
        if (discussion != null) {
            participantsIds = discussion.getParticipants().stream().map(medecin -> {
                return medecin.getId();
            }).collect(Collectors.toList());
            participantsIds.add(discussion.getMedcinResponsable().getId());
        }
        if (
            userId == payload.getSenderId() && 
            participantsIds.contains(userId) && 
            (discussion.getStatus().equals(DiscussionStatus.EN_COURS) || (eventName.equals("end") && discussion.getStatus().equals(DiscussionStatus.TERMINEE))) &&
            discussion.getType().equals(TypeDiscussion.APPEL_VIDEO)
        ) {
            participantsIds.forEach(participantId -> {
                if (participantId != userId) {
                    messagingTemplate.convertAndSendToUser(
                        participantId+"",
                        "/queue/"+eventName,
                        payload
                    );
                }
            });
        }
    }
}

package ma.inpt.esj.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.enums.DiscussionStatus;
import ma.inpt.esj.enums.TypeDiscussion;
import ma.inpt.esj.mappers.ChatMapper;
import ma.inpt.esj.repositories.DiscussionRepository;
import ma.inpt.esj.utils.ChatMessage;
import ma.inpt.esj.utils.ChatMessageResponse;
import ma.inpt.esj.utils.JwtUtil;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DiscussionChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final DiscussionRepository discussionRepository;
    private final ChatMapper chatMapper;
    private final JwtUtil jwtUtil;


    @Transactional
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        Jwt jwt = (Jwt) headerAccessor.getSessionAttributes().get("jwt");
        Long userId = jwtUtil.getUserIdFromJwt(jwt);
        Discussion discussion = discussionRepository.findById(chatMessage.getDiscussionId()).orElse(null);

        List<Long> participantsIds = null;
        
        if (discussion != null) {
            participantsIds = discussion.getParticipants().stream().map(medecin -> {
                return medecin.getId();
            }).collect(Collectors.toList());
            participantsIds.add(discussion.getMedcinResponsable().getId());
        }

        log.info("Receveid new message from: "+ chatMessage.getSenderId()+ " in discussion: "+ chatMessage.getDiscussionId());
        
        if (
            userId == chatMessage.getSenderId() && 
            participantsIds.contains(userId) && 
            discussion.getStatus().equals(DiscussionStatus.EN_COURS) &&
            discussion.getType().equals(TypeDiscussion.CHAT)
        ) {
            ChatMessageResponse chatMessageResponse = chatMapper.fromChatMessage(chatMessage);
            messagingTemplate.convertAndSend("/topic/discussion/" + chatMessage.getDiscussionId(), chatMessageResponse);
        }
    }

    @MessageMapping("/chat.endDiscussion")
    public void endDiscussion(@Payload Long discussionId, SimpMessageHeaderAccessor headerAccessor) {
        Jwt jwt = (Jwt) headerAccessor.getSessionAttributes().get("jwt");
        Long userId = jwtUtil.getUserIdFromJwt(jwt);
        Discussion discussion = discussionRepository.findById(discussionId).orElse(null);

        if (
            discussion != null &&
            userId == discussion.getMedcinResponsable().getId() && 
            discussion.getType().equals(TypeDiscussion.CHAT)
        ) {
            Map<String, Long> payload = new HashMap<>();
            payload.put("medcinConsulteId", discussion.getMedcinConsulte().getId());
            messagingTemplate.convertAndSend("/topic/discussion/" + discussionId + "/ended", payload);
        }
    }

    /* @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());
        messagingTemplate.convertAndSend("/topic/discussion/" + chatMessage.getDiscussionId(), chatMessage);
    } */

}

package ma.inpt.esj.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.repositories.MedecinRepository;
import ma.inpt.esj.utils.ChatMessage;
import ma.inpt.esj.utils.ChatMessageResponse;

@Service
@RequiredArgsConstructor
public class ChatMapper {
    private final MedecinRepository medecinRepository;
    private final MedecineMapper medecineMapper;
    
    public ChatMessageResponse fromChatMessage(ChatMessage chatMessage) {
        ChatMessageResponse chatMessageResponse = new ChatMessageResponse();
        BeanUtils.copyProperties(chatMessage, chatMessageResponse);
        Medecin medecin = medecinRepository.findById(chatMessage.getSenderId()).orElse(null);
        chatMessageResponse.setSender(medecineMapper.fromMedcine(medecin));
        return chatMessageResponse;
    }

}

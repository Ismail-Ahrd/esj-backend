package ma.inpt.esj.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ma.inpt.esj.entities.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{
    List<ChatMessage> findByChatId(String chatId);
}

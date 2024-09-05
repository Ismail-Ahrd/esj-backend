package ma.inpt.esj.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ma.inpt.esj.entities.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}

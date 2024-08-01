package ma.inpt.esj.services;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import ma.inpt.esj.utils.SignalMessage;

public interface DiscussionVideoChatService {
    void sendEvent(String eventName, SignalMessage payload, SimpMessageHeaderAccessor headerAccessor);
}

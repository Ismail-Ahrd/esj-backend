package ma.inpt.esj.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import ma.inpt.esj.services.DiscussionVideoChatService;
import ma.inpt.esj.utils.SignalMessage;

@Controller
@RequiredArgsConstructor
public class DiscussionVideoChatController {
    private final DiscussionVideoChatService discussionVideoChatService;

    @MessageMapping("/room.ready")
    public void onReady(@Payload SignalMessage payload, SimpMessageHeaderAccessor headerAccessor) {
        discussionVideoChatService.sendEvent("ready", payload, headerAccessor);
    }

    @MessageMapping("/room.candidate")
    public void candidate(@Payload SignalMessage payload, SimpMessageHeaderAccessor headerAccessor) {
        discussionVideoChatService.sendEvent("candidate", payload, headerAccessor);
    }

    @MessageMapping("/room.offer")
    public void offer(@Payload SignalMessage payload, SimpMessageHeaderAccessor headerAccessor) {
        discussionVideoChatService.sendEvent("offer", payload, headerAccessor);
    }

    @MessageMapping("/room.answer")
    public void answer(@Payload SignalMessage payload, SimpMessageHeaderAccessor headerAccessor) {
        discussionVideoChatService.sendEvent("answer", payload, headerAccessor);
    }

    @MessageMapping("/room.leave")
    public void leaveRoom(@Payload SignalMessage payload, SimpMessageHeaderAccessor headerAccessor) {
        discussionVideoChatService.sendEvent("leave", payload, headerAccessor);
    }

    @MessageMapping("/room.end")
    public void endRoom(@Payload SignalMessage payload, SimpMessageHeaderAccessor headerAccessor) {
        discussionVideoChatService.sendEvent("end", payload, headerAccessor);
    }

}

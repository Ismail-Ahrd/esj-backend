package ma.inpt.esj.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

    @MessageMapping("/test")
    @SendTo("/topic/public")
    public String test() {
        return "Hello World";
    }

}

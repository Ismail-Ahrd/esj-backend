package ma.inpt.esj.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String type;
    private String content;
    private Long senderId;
    private Long discussionId;
}


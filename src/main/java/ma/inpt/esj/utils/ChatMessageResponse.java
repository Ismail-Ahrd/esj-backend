package ma.inpt.esj.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.inpt.esj.dto.MedecinResponseDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {
    private String type;
    private String content;
    private MedecinResponseDTO sender;
    private Long discussionId;
}

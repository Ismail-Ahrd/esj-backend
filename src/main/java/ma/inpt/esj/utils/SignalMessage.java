package ma.inpt.esj.utils;

import lombok.Data;

@Data
public class SignalMessage {
    private String type;
    private Object sdp;
    private String candidate;
    private String id;
    private int label;
    private Long discussionId;
    private Long senderId;
}

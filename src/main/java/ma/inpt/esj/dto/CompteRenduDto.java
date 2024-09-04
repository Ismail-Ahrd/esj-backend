package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class CompteRenduDto {
    private Long id;
    private String conclusion;
    private Long discussionId;
}

package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.inpt.esj.enums.LiveEvaluation;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LiveFeedbackDTO {
	private int id;
	
	private LiveEvaluation evaluation;
	private boolean recommended;
	private String suggestedTheme;
	private String opinion;
}

package ma.inpt.esj.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.inpt.esj.dto.LiveFeedbackDTO;
import ma.inpt.esj.enums.LiveEvaluation;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LiveFeedback {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "generated")
	private int id;
	
	private LiveEvaluation evaluation;
	
	@Column(nullable = false)
	private boolean recommended;
	
	private String suggestedTheme;
	private String opinion;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="live_id")
	private Live live;
    
    @ManyToOne
    @JoinColumn(name="jeuneId")
    @JsonIgnore
    private Jeune jeune;
	
	
	public LiveFeedback(LiveFeedbackDTO dto, Live l, Jeune j) {
		this.id = dto.getId();
		this.evaluation = dto.getEvaluation();
		this.recommended = dto.isRecommended();
		this.suggestedTheme = dto.getSuggestedTheme();
		this.opinion = dto.getOpinion();
		this.setLive(l);
		this.setJeune(j);
	}
}

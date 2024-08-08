package ma.inpt.esj.services;

import java.util.List;
import java.util.Map;

import ma.inpt.esj.dto.LiveDTO;
import ma.inpt.esj.dto.LiveFeedbackDTO;
import ma.inpt.esj.enums.LiveEvaluation;

public interface LiveFeedbackService {

	public List<String> getOpinions(int liveId);
	
	public List<String> getSuggestedThemes(int liveId);
	
	public Map<LiveEvaluation, Integer> getEvaluation(int liveId);
	
	public Map<Boolean, Integer> getRecommended(int liveId);
	
	public void createFeedback ( LiveFeedbackDTO feedbackDTO, int liveId, Long jeuneId) throws Exception;
	
	public LiveDTO getLastLiveUnanswered( int jeuneId);
}

package ma.inpt.esj.services;

import java.util.List;

import ma.inpt.esj.dto.LiveDTO;
import ma.inpt.esj.dto.QuestionDTO;
import ma.inpt.esj.entities.Question;

public interface QuestionService {
    
    public List<QuestionDTO> getAllQuestions(int id);

    public void createOne(Question Q);
    
    public List<Question> getQuestionsByLiveIdAndJeuneId(int liveId, Long jeuneId);
	
	public List<LiveDTO> getonquestionsforuserId(Long jeuneId);

    public List<LiveDTO> getonfinalforuserId(Long jeuneId);
}

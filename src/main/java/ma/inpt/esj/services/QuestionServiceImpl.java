package ma.inpt.esj.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.inpt.esj.dto.LiveDTO;
import ma.inpt.esj.dto.QuestionDTO;
import ma.inpt.esj.entities.Question;
import ma.inpt.esj.mappers.QuestionMapper;
import ma.inpt.esj.repositories.QuestionRepository;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuestionMapper mapper;
    @Autowired
    private LiveService liveService;
    
    public List<QuestionDTO> getAllQuestions(int id){
    	return this.mapper.getallDtoQuestions(this.questionRepository.findByLiveId(id));
    }

    public void createOne(Question Q){
        this.questionRepository.save(Q);
    }

	@Override
	public List<Question> getQuestionsByLiveIdAndJeuneId(int liveId, Long jeuneId) {
		return this.questionRepository.findByLiveIdAndJeuneId(liveId, jeuneId);
	}
	
	@Override
	public List<LiveDTO> getonquestionsforuserId(Long jeuneId) {
		List<LiveDTO> L1=liveService.getAllfileattenteforuser();
        List<LiveDTO> Sending=new ArrayList<>();
        LocalDate D=LocalDate.now();

        for(int i=0;i<L1.size();i++){
        	List<Question> questionList = this.getQuestionsByLiveIdAndJeuneId(L1.get(i).getId(), jeuneId);
            //Period P=Period.between(D,L1.get(i).getDate().toLocalDate());
            System.out.println("id live question phase: "+L1.get(i).getId());
            System.out.println("size: "+questionList.size());
            //int days=P.getDays();
            long daysBetween = ChronoUnit.DAYS.between(D, L1.get(i).getDate().toLocalDate());

            System.out.println(daysBetween);
            if(daysBetween>3&&daysBetween<=28){
                Sending.add(L1.get(i));
            }
        }
        return Sending;
	}
	
	@Override
	public List<LiveDTO> getonfinalforuserId(Long jeuneId) {
		List<LiveDTO> L1=liveService.getAllfileattenteforuser();
        List<LiveDTO> L2=new ArrayList<>();
        LocalDateTime D=LocalDateTime.now();
        for(int i=0;i<L1.size();i++){
            long daysBetween = ChronoUnit.DAYS.between(D, L1.get(i).getDate());

            if(daysBetween<=3){
                L2.add(L1.get(i));
            }
        }
        return L2;
	}
}

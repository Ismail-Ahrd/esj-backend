package ma.inpt.esj.mappers;

import org.springframework.stereotype.Service;

import ma.inpt.esj.dto.QuestionDTO;
import ma.inpt.esj.entities.Question;

import java.util.ArrayList;
import java.util.List;
@Service
public class QuestionMapper {
    public List<QuestionDTO> getallDtoQuestions(List<Question> L){
        List<QuestionDTO> LD=new ArrayList<>();
        for(int i=0;i<L.size();i++){
            QuestionDTO P=new QuestionDTO();
            P.setId(L.get(i).getId());
            P.setContenu(L.get(i).getContenu());

            LD.add(P);
        }
        return LD;
    }

    public   QuestionDTO getSingle(Question T){
        QuestionDTO P=new QuestionDTO();
        P.setId(T.getId());
        P.setContenu(T.getContenu());

        return P;
    }
    public List<Question> Inverse(List<QuestionDTO> P){
        List<Question> LD=new ArrayList<>();
        for(int i=0;i<P.size();i++){
            Question TE=new Question();
            TE.setId(P.get(i).getId());
            TE.setContenu(P.get(i).getContenu());
            LD.add(TE);
        }
        return LD;
    }
    public  Question  Creating(QuestionDTO P){
        Question T=new Question();
        T.setContenu(P.getContenu());
        //T.setJeunes(P.getJeunes());
        return T;
    }
}

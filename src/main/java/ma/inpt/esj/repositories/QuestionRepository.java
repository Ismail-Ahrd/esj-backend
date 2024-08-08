package ma.inpt.esj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.inpt.esj.entities.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Integer> {
    public List<Question> findByLiveId(int id);
    public List<Question> findByLiveIdAndJeuneId(int liveId, int jeuneId);
}

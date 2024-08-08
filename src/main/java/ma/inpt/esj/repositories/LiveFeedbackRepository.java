package ma.inpt.esj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.inpt.esj.entities.LiveFeedback;

import java.util.List;


@Repository
public interface LiveFeedbackRepository extends JpaRepository<LiveFeedback, Integer>{
	List<LiveFeedback> findByLiveId(int id);
	List<LiveFeedback> findByLiveIdAndJeuneId(int liveId, int jeuneId);
}

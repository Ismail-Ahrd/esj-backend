package ma.inpt.esj.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.inpt.esj.exception.LiveNotFoundException;
import ma.inpt.esj.exception.UserNotFoundException;
import ma.inpt.esj.dto.LiveDTO;
import ma.inpt.esj.dto.LiveFeedbackDTO;
import ma.inpt.esj.entities.Jeune;
import ma.inpt.esj.entities.Live;
import ma.inpt.esj.entities.LiveFeedback;
import ma.inpt.esj.enums.LiveEvaluation;
import ma.inpt.esj.repositories.JeuneRepository;
import ma.inpt.esj.repositories.LiveFeedbackRepository;
import ma.inpt.esj.repositories.LiveRepository;

@Service
public class LiveFeedbackServiceImpl implements LiveFeedbackService {
	@Autowired
	private LiveFeedbackRepository liveFeedbackRepo;
	@Autowired
	private LiveRepository liveRepo;
	@Autowired
	JeuneService jeuneService;
	@Autowired
	LiveService liveService;
	@Autowired
	JeuneRepository jeuneRepository;

	public List<String> getOpinions(int liveId) {
		List<LiveFeedback> feedbackList = this.liveFeedbackRepo.findByLiveId(liveId);
		return feedbackList.stream()
				.map(LiveFeedback::getOpinion)
				.collect(Collectors.toList());
	}

	public List<String> getSuggestedThemes(int liveId) {
		List<LiveFeedback> feedbackList = this.liveFeedbackRepo.findByLiveId(liveId);
		return feedbackList.stream()
				.map(LiveFeedback::getSuggestedTheme)
				.collect(Collectors.toList());
	}

	public Map<LiveEvaluation, Integer> getEvaluation(int liveId) {
		List<LiveFeedback> feedbackList = this.liveFeedbackRepo.findByLiveId(liveId);
		Map<LiveEvaluation, Integer> evaluationCount = new HashMap<>();

		for (LiveEvaluation evaluation : LiveEvaluation.values()) {
			evaluationCount.put(evaluation, 0);
		}

		feedbackList.forEach(feedback -> {
			LiveEvaluation evaluation = feedback.getEvaluation();
			evaluationCount.put(evaluation, evaluationCount.get(evaluation) + 1);
		});

		return evaluationCount;
	}

	public Map<Boolean, Integer> getRecommended(int liveId) {
		List<LiveFeedback> feedbackList = this.liveFeedbackRepo.findByLiveId(liveId);
		Map<Boolean, Integer> recommendedCount = new HashMap<>();

		recommendedCount.put(true, 0);
		recommendedCount.put(false, 0);

		feedbackList.forEach(feedback -> {
			boolean recommended = feedback.isRecommended();
			recommendedCount.put(recommended, recommendedCount.get(recommended) + 1);
		});

		return recommendedCount;
	}

	public void createFeedback(LiveFeedbackDTO feedbackDTO, int liveId, Long jeuneId) throws Exception {
		Jeune jeune = jeuneRepository.getJeuneById(jeuneId);
		if (jeune == null)
			throw new UserNotFoundException("Le jeune d'id " + jeuneId + " est introvable");
		Live l = liveRepo.findById(liveId)
				.orElseThrow(() -> new LiveNotFoundException("Le live d'id " + liveId + " n'existe pas"));
		LiveFeedback feedback = new LiveFeedback(feedbackDTO, l, jeune);
		this.liveFeedbackRepo.save(feedback);
	}

	@Override
	public LiveDTO getLastLiveUnanswered(Long jeuneId) {
		LiveDTO live = this.liveService.getLastLive();
		if (live == null) {
			return null;
		}
		List<LiveFeedback> feedbacks = this.liveFeedbackRepo.findByLiveIdAndJeuneId(live.getId(), jeuneId);
		if (feedbacks.isEmpty()) {
			return live;
		}
		return null;
	}

	@Override
	public LiveDTO getCurrentlyAiringLive() {
		return this.liveService.getOngoingLive();
	}
}

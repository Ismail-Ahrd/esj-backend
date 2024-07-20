package ma.inpt.esj.services;

import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.enums.DiscussionStatus;
import ma.inpt.esj.exception.DiscussionException;
import ma.inpt.esj.exception.DiscussionNotFoundException;
import ma.inpt.esj.exception.MedecinNotFoundException;
import ma.inpt.esj.repositories.DiscussionRepository;
import ma.inpt.esj.repositories.MedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ma.inpt.esj.enums.DiscussionStatus.EN_COURS;

@Service
public class DiscussionServiceImpl implements DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final MedecinRepository medecinRepository;

    @Autowired
    public DiscussionServiceImpl(DiscussionRepository discussionRepository, MedecinRepository medecinRepository) {
        this.discussionRepository = discussionRepository;
        this.medecinRepository = medecinRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Discussion> getAllDiscussions() throws DiscussionException {
        try {
            return discussionRepository.findAll();
        } catch (Exception e) {
            throw new DiscussionException("Erreur lors de la récupération des discussions", e);
        }
    }

    @Override
    @Transactional
    public Discussion createDiscussion(Discussion discussion) throws DiscussionException {
        if (discussion == null) {
            throw new IllegalArgumentException("La discussion ne doit pas être nulle.");
        }
        try {
            return discussionRepository.save(discussion);
        } catch (Exception e) {
            throw new DiscussionException("Erreur lors de l'enregistrement de la discussion", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Discussion getDiscussion(Long id) throws DiscussionNotFoundException {
        return discussionRepository.findById(id)
                .orElseThrow(() -> new DiscussionNotFoundException("La discussion avec l'identifiant " + id + " non trouvée."));
    }

    @Override
    @Transactional
    public Discussion startDiscussion(Long id) throws DiscussionNotFoundException, DiscussionException {
        Discussion discussion = getDiscussion(id);
        discussion.setStatus(EN_COURS);
        try {
            return discussionRepository.save(discussion);
        } catch (Exception e) {
            throw new DiscussionException("Erreur lors du lancement de la discussion", e);
        }
    }

    @Override
    @Transactional
    public Discussion joinDiscussion(Long id, Long medecinId) throws DiscussionNotFoundException, MedecinNotFoundException, DiscussionException {
        Discussion discussion = getDiscussion(id);

        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new MedecinNotFoundException("Le médecin avec l'identifiant " + medecinId + " non trouvé."));

        discussion.addMedecin(medecin);

        try {
            return discussionRepository.save(discussion);
        } catch (Exception e) {
            throw new DiscussionException("Erreur lors de l'ajout du médecin à la discussion", e);
        }
    }

    @Override
    @Transactional (readOnly = true)
    public List<Discussion> getDiscussionsByMedecinSpecialite(Long medecinId) throws MedecinNotFoundException {
        try {
            return discussionRepository.findDiscussionsByMedecinSpecialite(medecinId, DiscussionStatus.PLANIFIEE);

        }
        catch (Exception e){
        throw  new MedecinNotFoundException("Le médecin avec l'identifiant " + medecinId + " non trouvé." , e);
        }
    }

    @Override
    @Transactional (readOnly = true)
    public List<Discussion> getDiscussionByMedecinResponsable(Long medecinId) throws MedecinNotFoundException {
        try{
            return discussionRepository.findDiscussionsByStatusAndMedcinResponsable_Id(DiscussionStatus.PLANIFIEE, medecinId);
        }
        catch (Exception e){
            throw  new MedecinNotFoundException("Le médecin avec l'identifiant " + medecinId + " non trouvé." , e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Discussion> getByParticipantId(Long medecinId) throws MedecinNotFoundException {
        try {
            List<DiscussionStatus> status = Arrays.asList(DiscussionStatus.EN_COURS, DiscussionStatus.PLANIFIEE);
            return discussionRepository.findByParticipantIdAndStatus(medecinId, status);
        } catch (Exception e) {
            throw new MedecinNotFoundException("Le médecin avec l'identifiant " + medecinId + " n'a pas été trouvé.", e);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<Discussion> getFinishedDiscussionsByParticipantId(Long medecinId) throws MedecinNotFoundException {
        try {
            List<DiscussionStatus> statuses = Arrays.asList(DiscussionStatus.TERMINEE);
            List<Discussion> discussions = new ArrayList<>();

            discussions.addAll(discussionRepository.findDiscussionsByStatusAndMedcinResponsable_Id(DiscussionStatus.TERMINEE, medecinId));

            discussions.addAll(discussionRepository.findByParticipantIdAndStatus(medecinId, statuses));

            return discussions;
        } catch (Exception e) {
            throw new MedecinNotFoundException("Le médecin avec l'identifiant " + medecinId + " n'a pas été trouvé.", e);
        }
    }




}

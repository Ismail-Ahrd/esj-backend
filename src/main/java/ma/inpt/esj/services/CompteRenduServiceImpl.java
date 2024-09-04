package ma.inpt.esj.services;

import ma.inpt.esj.dto.CompteRenduDto;
import ma.inpt.esj.entities.CompteRendu;
import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.exception.CompteRenduException;
import ma.inpt.esj.exception.CompteRenduNotFoundException;
import ma.inpt.esj.exception.DiscussionNotFoundException;
import ma.inpt.esj.mappers.CompteRenduMapper;
import ma.inpt.esj.repositories.CompteRenduRepository;
import ma.inpt.esj.repositories.DiscussionRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompteRenduServiceImpl implements CompteRenduService {

    private final CompteRenduRepository compteRenduRepository;
    private final DiscussionRepository discussionRepository;
    private final CompteRenduMapper compteRenduMapper;

    @Autowired
    public CompteRenduServiceImpl(
        CompteRenduRepository compteRenduRepository, 
        DiscussionRepository discussionRepository, 
        CompteRenduMapper compteRenduMapper
    ) {
        this.compteRenduRepository = compteRenduRepository;
        this.discussionRepository = discussionRepository;
        this.compteRenduMapper = compteRenduMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public CompteRenduDto getCompteRenduById(Long id, Long userId) throws CompteRenduNotFoundException, DiscussionNotFoundException, CompteRenduException {

        CompteRendu compteRendu = compteRenduRepository.findById(id)
            .orElseThrow(() -> new CompteRenduNotFoundException("Compte Rendu avec l'identifiant " + id + " non trouvé."));

        Discussion discussion = compteRendu.getDiscussion();
        if (discussion == null) {
            throw new DiscussionNotFoundException("Il n'existe aucune Discussion avec l'id: "+ compteRendu.getDiscussion().getId());
        }

        List<Long> participantsIds = discussion.getParticipants().stream().map(medecin -> {
            return medecin.getId();
        }).collect(Collectors.toList());
        participantsIds.add(discussion.getMedcinResponsable().getId());

        if (!participantsIds.contains(userId)) {
            throw new CompteRenduException("L'utilisateur n'est pas un participant à cette discussion.");
        }
       
        return compteRenduMapper.fromCompteRendu(compteRendu);
    }

    @Override
    @Transactional
    public CompteRenduDto createCompteRendu(Long userId, CompteRenduDto c) throws CompteRenduException, DiscussionNotFoundException {
        if (c == null) {
            throw new CompteRenduException("Le compte rendu ne doit pas être nul.");
        }

        Discussion discussion = discussionRepository.findById(c.getDiscussionId()).orElse(null);
        if (discussion == null) {
            throw new DiscussionNotFoundException("Il n'existe aucune Discussion avec l'id: "+ c.getDiscussionId() );
        }

        if (userId != discussion.getMedcinConsulte().getId()) {
            throw new CompteRenduException("Seulement le médecin consulté peut créer une compte rendu.");
        }

        try {
            CompteRendu savedCompteRendu = compteRenduRepository.save(compteRenduMapper.fromCompteRendudTO(c));
            return compteRenduMapper.fromCompteRendu(savedCompteRendu);
        } catch (Exception e) {
            throw new CompteRenduException("Erreur lors de l'enregistrement du Compte Rendu", e);
        }
    }

    @Override
    @Transactional
    public CompteRendu updateCompteRendu(Long id, CompteRendu c) throws CompteRenduNotFoundException, CompteRenduException {
        CompteRendu existingCompteRendu = compteRenduRepository.findById(id)
                .orElseThrow(() -> new CompteRenduNotFoundException("Compte Rendu avec l'identifiant " + id + " non trouvé."));

        existingCompteRendu.setConclusion(c.getConclusion());
        existingCompteRendu.setDiscussion(c.getDiscussion());

        try {
            return compteRenduRepository.save(existingCompteRendu);
        } catch (Exception e) {
            throw new CompteRenduException("Erreur lors de la mise à jour du Compte Rendu", e);
        }
    }
}

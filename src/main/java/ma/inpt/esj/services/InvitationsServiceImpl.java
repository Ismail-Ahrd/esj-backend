package ma.inpt.esj.services;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ma.inpt.esj.entities.Invitation;
import ma.inpt.esj.enums.InvitationStatus;
import ma.inpt.esj.exception.InvitationException;
import ma.inpt.esj.exception.InvitationNotFoundException;
import ma.inpt.esj.exception.MedecinNotFoundException;
import ma.inpt.esj.repositories.InvitationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ma.inpt.esj.enums.InvitationStatus.ACCEPTE;
import static ma.inpt.esj.enums.InvitationStatus.REFUSE;

@Service
public class InvitationsServiceImpl implements InvitationsService {

    private final InvitationRepository invitationRepository;

    public InvitationsServiceImpl(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    @Override
    @Transactional
    public Invitation createInvitation(@Valid @NotNull Invitation invitation) throws InvitationException {
        try {
            return invitationRepository.save(invitation);
        } catch (Exception e) {
            throw new InvitationException("Erreur lors de l'enregistrement de l'invitation", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Invitation getInvitation(Long id) throws InvitationNotFoundException {
        return invitationRepository.findById(id)
                .orElseThrow(() -> new InvitationNotFoundException("L'invitation avec l'identifiant " + id + " n'est pas trouvée."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invitation> getInvitations() throws InvitationException {
        try {
            return invitationRepository.findAll();
        } catch (Exception e) {
            throw new InvitationException("Erreur lors de la récupération des invitations", e);
        }
    }

    @Override
    @Transactional
    public Invitation acceptInvitation(Long id) throws InvitationException, InvitationNotFoundException {
        Invitation invitation = getInvitation(id);
        invitation.setStatus(ACCEPTE);
        try {
            return invitationRepository.save(invitation);
        } catch (Exception e) {
            throw new InvitationException("Erreur lors de l'acceptation de l'invitation", e);
        }
    }

    @Override
    @Transactional
    public Invitation declineInvitation(Long id) throws InvitationException, InvitationNotFoundException {
        Invitation invitation = getInvitation(id);
        invitation.setStatus(REFUSE);
        try {
            return invitationRepository.save(invitation);
        } catch (Exception e) {
            throw new InvitationException("Erreur lors du refus de l'invitation", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invitation> getByMedecinIdAndStatusInDiscussion(Long medecinId) throws MedecinNotFoundException {
        try{
            return invitationRepository.findByMedecinIdAndStatusInDiscussion(medecinId, InvitationStatus.INVITEE);
        } catch (Exception e ){
            throw new MedecinNotFoundException("Le médecin avec l'identifiant " + medecinId + " n'a pas été trouvé.", e);
        }
    }
}

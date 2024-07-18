package ma.inpt.esj.services;

import ma.inpt.esj.entities.Invitation;
import ma.inpt.esj.exception.DiscussionException;
import ma.inpt.esj.exception.InvitationException;
import ma.inpt.esj.exception.InvitationNotFoundException;
import ma.inpt.esj.exception.MedecinNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InvitationsService {
    Invitation createInvitation(Invitation invitation) throws InvitationException;
    Invitation getInvitation(Long id) throws InvitationNotFoundException;
    List<Invitation> getInvitations() throws InvitationException;
    Invitation acceptInvitation(Long id) throws InvitationException, InvitationNotFoundException, DiscussionException;
    Invitation declineInvitation(Long id) throws InvitationException, InvitationNotFoundException, DiscussionException;

    @Transactional(readOnly = true)
    List<Invitation> getByMedecinIdAndStatusInDiscussion(Long medecinId) throws MedecinNotFoundException;
}

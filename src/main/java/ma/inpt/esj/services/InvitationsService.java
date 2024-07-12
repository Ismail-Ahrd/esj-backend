package ma.inpt.esj.services;

import ma.inpt.esj.entities.Invitation;
import ma.inpt.esj.exception.InvitationException;
import ma.inpt.esj.exception.InvitationNotFoundException;

import java.util.List;

public interface InvitationsService {
    Invitation createInvitation(Invitation invitation) throws InvitationException;
    Invitation getInvitation(Long id) throws InvitationNotFoundException;
    List<Invitation> getInvitations() throws InvitationException;
    Invitation acceptInvitation(Long id) throws InvitationException, InvitationNotFoundException;
    Invitation declineInvitation(Long id) throws InvitationException, InvitationNotFoundException;
}

package ma.inpt.esj.services;

import ma.inpt.esj.dto.InvitationDto;
import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.entities.Invitation;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.enums.InvitationStatus;
import ma.inpt.esj.exception.DiscussionException;
import ma.inpt.esj.exception.InvitationException;
import ma.inpt.esj.exception.InvitationNotFoundException;
import ma.inpt.esj.mappers.InvitationMapper;
import ma.inpt.esj.repositories.DiscussionRepository;
import ma.inpt.esj.repositories.InvitationRepository;
import ma.inpt.esj.repositories.MedecinRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static ma.inpt.esj.enums.InvitationStatus.ACCEPTE;
import static ma.inpt.esj.enums.InvitationStatus.REFUSE;

@Service
public class InvitationsServiceImpl implements InvitationsService {

    private final InvitationRepository invitationRepository;
    private final DiscussionRepository discussionRepository;
    private final InvitationMapper invitationMapper;
    private final MedecinRepository medecinRepository;

    public InvitationsServiceImpl(
        InvitationRepository invitationRepository, 
        DiscussionRepository discussionRepository,
        InvitationMapper invitationMapper,
        MedecinRepository medecinRepository
    ) {
        this.invitationRepository = invitationRepository;
        this.discussionRepository =discussionRepository;
        this.invitationMapper = invitationMapper;
        this.medecinRepository = medecinRepository;
    }

    /* @Override
    @Transactional
    public Invitation createInvitation(@Valid @NotNull Invitation invitation) throws InvitationException {
        try {
            return invitationRepository.save(invitation);
        } catch (Exception e) {
            throw new InvitationException("Erreur lors de l'enregistrement de l'invitation", e);
        }
    } */

    @Override
    @Transactional(readOnly = true)
    public Invitation getInvitation(Long id) throws InvitationNotFoundException {
        return invitationRepository.findById(id)
                .orElseThrow(() -> new InvitationNotFoundException("L'invitation avec l'identifiant " + id + " n'est pas trouvée."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvitationDto> getInvitations() throws InvitationException {
        try {
            List<Invitation> invitations = invitationRepository.findAll();
            List<InvitationDto> invitationDtos = new ArrayList<>();
            invitations.forEach((inv) -> {
                invitationDtos.add(invitationMapper.fromInvitation(inv));
            });
            return invitationDtos;
        } catch (Exception e) {
            throw new InvitationException("Erreur lors de la récupération des invitations", e);
        }
    }

    @Override
    public List<InvitationDto> getMyInvitations(Long id, InvitationStatus status) throws InvitationException {
        Medecin medecin = medecinRepository.findById(id).get();
        try {
            List<Invitation> invitations = null;
            System.out.println("status: "+ status);
            if (status == null || status.equals("")) {
                invitations = invitationRepository.findByMedecinInvite(medecin);
            } else {
                invitations = invitationRepository.findByMedecinInviteAndStatus(medecin, status);
            }
            List<InvitationDto> invitationDtos = new ArrayList<>();
            invitations.forEach((inv) -> {
                invitationDtos.add(invitationMapper.fromInvitation(inv));
            });
            return invitationDtos;
        } catch (Exception e) {
            throw new InvitationException("Erreur lors de la récupération des invitations", e);
        }
    }

    @Override
    @Transactional
    public InvitationDto acceptInvitation(Long id, Long userId) throws InvitationException, InvitationNotFoundException , DiscussionException{
        
        Invitation invitation = getInvitation(id);
        
        if(invitation.getMedecinInvite().getId() != userId) {
            throw new InvitationException("Seulement les Medecins invitées peuvent accepter une invitation");
        }

        invitation.setStatus(ACCEPTE);

        Discussion discussion = invitation.getDiscussion();
        if (discussion != null) {
            discussion.addMedecin(invitation.getMedecinInvite());
            discussionRepository.save(discussion);
        } else {
            throw new DiscussionException("Discussion non trouve");
        }


        try {
            Invitation savedInvitation = invitationRepository.save(invitation);
            InvitationDto invitationDto = invitationMapper.fromInvitation(savedInvitation);
            return invitationDto;
        } catch (Exception e) {
            throw new InvitationException("Erreur lors de l'acceptation de l'invitation", e);
        }
    }

    @Override
    @Transactional
    public InvitationDto declineInvitation(Long id, Long userId) throws InvitationException, InvitationNotFoundException,DiscussionException {
        Invitation invitation = getInvitation(id);

        if(invitation.getMedecinInvite().getId() != userId) {
            throw new InvitationException("Seulement les Medecins invitées peuvent accepter une invitation");
        }

        invitation.setStatus(REFUSE);
        try {
            Invitation savedInvitation = invitationRepository.save(invitation);
            InvitationDto invitationDto = invitationMapper.fromInvitation(savedInvitation);
            return invitationDto;
        } catch (Exception e) {
            throw new InvitationException("Erreur lors du refus de l'invitation", e);
        }
    }
}

package ma.inpt.esj.controllers;

import jakarta.validation.Valid;
import ma.inpt.esj.entities.Invitation;
import ma.inpt.esj.exception.InvitationException;
import ma.inpt.esj.exception.InvitationNotFoundException;
import ma.inpt.esj.exception.MedecinNotFoundException;
import ma.inpt.esj.services.InvitationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

    private final InvitationsService invitationsService;

    public InvitationController(InvitationsService invitationsService) {
        this.invitationsService = invitationsService;
    }

    @GetMapping
    public ResponseEntity<?> getInvitations() {
        try {
            List<Invitation> invitations = invitationsService.getInvitations();
            return ResponseEntity.ok(invitations);
        } catch (InvitationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping
    public ResponseEntity<?> createInvitation(@Valid @RequestBody Invitation invitation) {
        try {
            Invitation createdInvitation = invitationsService.createInvitation(invitation);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInvitation);
        } catch (InvitationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable Long id) {
        try {
            Invitation invitation = invitationsService.acceptInvitation(id);
            return ResponseEntity.ok(invitation);
        } catch (InvitationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (InvitationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<?> declineInvitation(@PathVariable Long id) {
        try {
            Invitation invitation = invitationsService.declineInvitation(id);
            return ResponseEntity.ok(invitation);
        } catch (InvitationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (InvitationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{medecinId}")
    public ResponseEntity<?> getMedecinInvitations(@PathVariable Long medecinId) {
        try {
            List<Invitation> invitations = invitationsService.getByMedecinIdAndStatusInDiscussion(medecinId);
            return ResponseEntity.ok(invitations);
        } catch (MedecinNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}

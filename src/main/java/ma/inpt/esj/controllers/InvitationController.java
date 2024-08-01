package ma.inpt.esj.controllers;

import ma.inpt.esj.dto.InvitationDto;
import ma.inpt.esj.enums.InvitationStatus;
import ma.inpt.esj.exception.DiscussionException;
import ma.inpt.esj.exception.InvitationException;
import ma.inpt.esj.exception.InvitationNotFoundException;
import ma.inpt.esj.services.InvitationsService;
import ma.inpt.esj.utils.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

    private final InvitationsService invitationsService;
    private final JwtUtil jwtUtil;

    public InvitationController(InvitationsService invitationsService, JwtUtil jwtUtil) {
        this.invitationsService = invitationsService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getInvitations() {
        try {
            List<InvitationDto> invitations = invitationsService.getInvitations();
            return ResponseEntity.ok(invitations);
        } catch (InvitationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getMyInvitations(
        @RequestParam(name = "status", defaultValue = "")  InvitationStatus status
    ) {
        Long userId = jwtUtil.getUserIdFromJwt();
        try {
            List<InvitationDto> invitations = invitationsService.getMyInvitations(userId, status);
            return ResponseEntity.ok(invitations);
        } catch (InvitationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    /* @PostMapping
    public ResponseEntity<?> createInvitation(@Valid @RequestBody Invitation invitation) {
        try {
            Invitation createdInvitation = invitationsService.createInvitation(invitation);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInvitation);
        } catch (InvitationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }*/

    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable Long id) {
        Long userId = jwtUtil.getUserIdFromJwt();
        try {
            InvitationDto invitation = invitationsService.acceptInvitation(id, userId);
            return ResponseEntity.ok(invitation);
        } catch (InvitationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (InvitationNotFoundException | DiscussionException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<?> declineInvitation(@PathVariable Long id) {
        Long userId = jwtUtil.getUserIdFromJwt();
        try {
            InvitationDto invitation = invitationsService.declineInvitation(id, userId);
            return ResponseEntity.ok(invitation);
        } catch (InvitationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (InvitationNotFoundException | DiscussionException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /* @GetMapping("/{medecinId}")
    public ResponseEntity<?> getMedecinInvitations(@PathVariable Long medecinId) {
        try {
            List<Invitation> invitations = invitationsService.getByMedecinIdAndStatusInDiscussion(medecinId);
            return ResponseEntity.ok(invitations);
        } catch (MedecinNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    } */


}

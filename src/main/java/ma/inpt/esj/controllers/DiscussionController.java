package ma.inpt.esj.controllers;

import ma.inpt.esj.dto.DiscussionRequestDto;
import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.exception.DiscussionException;
import ma.inpt.esj.exception.DiscussionNotFoundException;
import ma.inpt.esj.exception.MedecinNotFoundException;
import ma.inpt.esj.services.DiscussionService;
import ma.inpt.esj.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discussion")
public class DiscussionController {

    private final DiscussionService discussionService;
    private final JwtUtil jwtUtil;

    @Autowired
    public DiscussionController(DiscussionService discussionService, JwtUtil jwtUtil) {
        this.discussionService = discussionService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<?> getAllDiscussions() {
        try {
            Iterable<Discussion> discussions = discussionService.getAllDiscussions();
            return ResponseEntity.ok(discussions);
        } catch (DiscussionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDiscussionById(@PathVariable Long id) {
        try {
            Discussion discussion = discussionService.getDiscussion(id);
            return ResponseEntity.ok(discussion);
        } catch (DiscussionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createDiscussion(@RequestBody DiscussionRequestDto discussionRequestDto) {
        try {
            Discussion d = discussionService.createDiscussion(discussionRequestDto);
            return ResponseEntity.ok(d);
        } catch (DiscussionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<?> startDiscussion(@PathVariable Long id) {
        Long userId = jwtUtil.getUserIdFromJwt();
        try {
            Discussion d = discussionService.startDiscussion(id, userId);
            return ResponseEntity.ok(d);
        } catch (DiscussionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DiscussionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinDiscussion(@PathVariable Long id) {
        Long medecinId = jwtUtil.getUserIdFromJwt();
        try {
            Discussion d = discussionService.joinDiscussion(id, medecinId);
            return ResponseEntity.ok(d);
        } catch (MedecinNotFoundException | DiscussionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DiscussionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/byMedecinSpecialite/{medecinId}")
    public ResponseEntity<?> getDiscussionsByMedecinSpecialite(@PathVariable Long medecinId) {
        try {
            List<Discussion> d = discussionService.getDiscussionsByMedecinSpecialite(medecinId);
            return ResponseEntity.ok(d);
        }  catch (MedecinNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/discussionsCrees/{medecinId}")
    public ResponseEntity<?> getDiscussionsCrees(@PathVariable Long medecinId) {
        try {
            List<Discussion> d = discussionService.getDiscussionByMedecinResponsable(medecinId);
            return ResponseEntity.ok(d);
        }  catch (MedecinNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/discussionsPlanifiees/{medecinId}")
    public ResponseEntity<?> getDiscussionsPlanifiees(@PathVariable Long medecinId) {
        try {
            List<Discussion> d = discussionService.getByParticipantId(medecinId);
            return ResponseEntity.ok(d);
        }  catch (MedecinNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/discussionsTerminees/{medecinId}")
    public ResponseEntity<?> getDiscussionsterminees(@PathVariable Long medecinId) {
        try {
            List<Discussion> d = discussionService.getFinishedDiscussionsByParticipantId(medecinId);
            return ResponseEntity.ok(d);
        }  catch (MedecinNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }











}

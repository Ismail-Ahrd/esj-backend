package ma.inpt.esj.controllers;

import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.exception.DiscussionException;
import ma.inpt.esj.exception.DiscussionNotFoundException;
import ma.inpt.esj.exception.MedecinNotFoundException;
import ma.inpt.esj.services.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discussion")
public class DiscussionController {

    private final DiscussionService discussionService;

    @Autowired
    public DiscussionController(DiscussionService discussionService) {
        this.discussionService = discussionService;
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
    public ResponseEntity<?> createDiscussion(@RequestBody Discussion discussion) {
        try {
            Discussion d = discussionService.createDiscussion(discussion);
            return ResponseEntity.ok(d);
        } catch (DiscussionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<?> startDiscussion(@PathVariable Long id) {
        try {
            Discussion d = discussionService.startDiscussion(id);
            return ResponseEntity.ok(d);
        } catch (DiscussionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DiscussionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinDiscussion(@PathVariable Long id, @RequestParam Long medecinId) {
        try {
            Discussion d = discussionService.joinDiscussion(id, medecinId);
            return ResponseEntity.ok(d);
        } catch (MedecinNotFoundException | DiscussionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DiscussionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

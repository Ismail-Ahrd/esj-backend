package ma.inpt.esj.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ma.inpt.esj.exception.LiveNotFoundException;
import ma.inpt.esj.exception.UserNotFoundException;
import ma.inpt.esj.dto.QuestionDTO;
import ma.inpt.esj.entities.Jeune;
import ma.inpt.esj.entities.Live;
import ma.inpt.esj.entities.Question;
import ma.inpt.esj.repositories.JeuneRepository;
import ma.inpt.esj.repositories.LiveRepository;
import ma.inpt.esj.services.JeuneService;
import ma.inpt.esj.services.QuestionService;

@RestController
public class QuestionController {
    @Autowired
    QuestionService service;
    @Autowired
    LiveRepository liveRepository;
    @Autowired
    JeuneService jeuneService;
    @Autowired
    JeuneRepository jeuneRepository;

    @GetMapping("/streams/{id}/questions")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(@PathVariable int id){
		try {
			List<QuestionDTO> list_questionses = this.service.getAllQuestions(id);
			if (list_questionses.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			return ResponseEntity.status(HttpStatus.OK).body(list_questionses);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
    }
    
    @PostMapping("/jeune/{jeuneId}/streams/{id}/questions")
    public ResponseEntity<String> createOne(@RequestBody Question Q, @PathVariable int id, @PathVariable Long jeuneId) {
        try {
        	Jeune jeune = jeuneRepository.getJeuneById(jeuneId);
        	if (jeune == null)
        		throw new UserNotFoundException("Le jeune d'id "+id+" est introvable");
        	Live l = liveRepository.findById(id)
        			.orElseThrow(() -> new LiveNotFoundException("Le live d'id "+id+" est introvable"));
        	Q.setJeune(jeune);
        	Q.setLive(l);
        	this.service.createOne(Q);
            return ResponseEntity.status(HttpStatus.CREATED).body("Question created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating Question: "+e.getMessage());
        }
    }
}

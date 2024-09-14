package ma.inpt.esj.controllers;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import ma.inpt.esj.dto.ConsultationDTO;
import ma.inpt.esj.dto.JeuneDto;
import ma.inpt.esj.dto.LiveDTO;
import ma.inpt.esj.dto.LiveFeedbackDTO;
import ma.inpt.esj.entities.*;
import ma.inpt.esj.exception.EmailNonValideException;
import ma.inpt.esj.exception.JeuneNotFoundException;
import ma.inpt.esj.exception.PhoneNonValideException;
import ma.inpt.esj.repositories.JeuneRepository;
import ma.inpt.esj.services.JeuneService;
import ma.inpt.esj.utils.JwtUtil;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.websocket.server.PathParam;
import ma.inpt.esj.services.JeuneServiceImpl;
import ma.inpt.esj.services.LiveFeedbackService;
import ma.inpt.esj.services.QuestionService;

@RestController
@AllArgsConstructor
public class JeuneController {

    private static final Logger logger = Logger.getLogger(JeuneController.class.getName());
    @Autowired
    private JeuneService jeuneService;
    @Autowired
    QuestionService questionService;
    @Autowired
    LiveFeedbackService liveFeedbackService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    private JeuneServiceImpl jeuneServiceImpl;
    @Autowired
    private JeuneRepository jeuneRepository;

    @GetMapping("/jeunes")
    public List<Jeune> getAllJeunes() {
        return jeuneService.getAllJeunes();

    }
    @GetMapping("/jeunes_filter")
    public List<Jeune> getAllJeunes(@RequestParam(required = false) Long id,
                                    @RequestParam(required = false) String nom,
                                    @RequestParam(required = false) String prenom) {
        return jeuneService.getFiltredJeunes(id, nom, prenom);

    }
        @GetMapping("/jeunes/{id}")
//    @PreAuthorize("hasRole('ROLE_JEUNE')")
    public ResponseEntity<?> getJeuneById(@PathVariable(value = "id") Long id) throws Exception {
//        System.out.println("id from jwt token "+jwtUtil.getUserIdFromJwt());
//        System.out.println("id from request url "+id);
        if(true){
            try {
                Object jeune = jeuneService.getJeuneById(id);
                return ResponseEntity.ok().body(jeune);
            } catch (JeuneNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
       } else {
            throw new Exception("You are not authorized to access this profile.");
        }
    }
    @GetMapping("/jeunes/data1/{id}")
    public ResponseEntity<?> getJeuneDataById(@PathVariable(value = "id") Long id) {
        Jeune jeune = null;
        try {
            jeune = jeuneService.getJeuneById2(id);
            //String res = jeuneService.sendJeuneToKafka(jeune);
            return ResponseEntity.ok().body(jeune);
        } catch (JeuneNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/register/jeunes/scolarise")
    public ResponseEntity<JeuneDto> saveJeuneScolarise(@RequestBody JeuneScolarise jeuneScolarise) throws EmailNonValideException, PhoneNonValideException {

        System.out.println("****************************");
        System.out.println(jeuneScolarise.getCne());
        System.out.println("*************************************");

        JeuneDto savedJeune = jeuneService.saveJeune(jeuneScolarise);
        jeuneService.sendJeuneToKafka((Jeune)jeuneScolarise);
        return ResponseEntity.ok(savedJeune);

    }

    @PostMapping("/register/jeunes/nonscolarise")
    public ResponseEntity<JeuneDto> saveJeuneNonScolarise(@RequestBody JeuneNonScolarise jeuneNonScolarise) {
        try {
            JeuneDto savedJeune = jeuneService.saveJeune(jeuneNonScolarise);
            jeuneService.sendJeuneToKafka((Jeune)jeuneNonScolarise);
            return ResponseEntity.ok(savedJeune);
        } catch (EmailNonValideException | PhoneNonValideException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PatchMapping("/jeunes/{id}")
    public ResponseEntity<?> patchMedecin(@PathVariable Long id, @RequestBody Map<String, Object> updates)  {
        try {
            JeuneDto updateJeunePartial = jeuneService.updateJeunePartial(id, updates);
            return ResponseEntity.ok(updateJeunePartial);
        }catch (JeuneNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/jeunes/{id}")
    public ResponseEntity<Void> deleteJeune(@PathVariable Long id) {
        jeuneService.deleteJeune(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/jeunes/{id}/consultations")
    public ResponseEntity<Jeune> addConsultationToJeune(@PathVariable Long id,
                                                        @RequestBody ConsultationDTO consultationDTO) {
        Jeune jeune = jeuneService.addConsultationDTOToJeune(id, consultationDTO);
        if (jeune == null) {
            return ResponseEntity.notFound().build();
        }
        //jeuneService.sendJeuneToKafka(jeune);
        return ResponseEntity.ok(jeune);
    }

    @PutMapping("/jeunes/{id}/consultations/{idConsultation}")
    public ResponseEntity<Jeune> updateConsultationOfJeune(@PathVariable Long id,
                                                           @RequestBody ConsultationDTO consultationDTO, @PathVariable Long idConsultation) {
        Jeune jeune = jeuneService.updateConsultationDTOJeune(id, consultationDTO, idConsultation);
        if (jeune == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jeune);
    }

    @PostMapping("/jeunes/{id}/antecedentsFamiliaux")
    public ResponseEntity<Jeune> addAntecedentFamilialToJeune(@PathVariable Long id,
                                                              @RequestBody AntecedentFamilial antecedentFamilial) {
        Jeune jeune = jeuneService.addAntecedentFamilialToJeune(id, antecedentFamilial);
        if (jeune == null) {
            return ResponseEntity.notFound().build();
        }
        //jeuneService.sendJeuneToKafka(jeune);
        return ResponseEntity.ok(jeune);
    }

    @PostMapping("/jeunes/{id}/antecedentsPersonnels")
    public ResponseEntity<Jeune> addAntecedentPersonnelToJeune(@PathVariable Long id,
                                                               @RequestBody AntecedentPersonnel antecedentPersonnel) {
        Jeune jeune = jeuneService.addAntecedentPersonnelToJeune(id, antecedentPersonnel);
        if (jeune == null) {
            return ResponseEntity.notFound().build();
        }
        //jeuneService.sendJeuneToKafka(jeune);
        return ResponseEntity.ok(jeune);
    }
    @GetMapping("/jeunes/{jeuneId}/antecedentsFamiliaux")
    public ResponseEntity<Map<String, List<String>>> getAntecedentFamiliaux(@PathVariable Long jeuneId) {
        try {
            Map<String, List<String>> antecedentFamiliaux = jeuneService.getAntecedentFamilByJeuneId(jeuneId);
            return new ResponseEntity<>(antecedentFamiliaux, HttpStatus.OK);
        } catch (JeuneNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/jeunes/{jeuneId}/antecedentsPersonnels")
    public ResponseEntity<Map<String, Object>> getAntecedentPersonnels(@PathVariable Long jeuneId) {
        try {
            Map<String, Object> antecedentPersonnels = jeuneService.getAntecedentPersonelByJeuneId(jeuneId);
            return new ResponseEntity<>(antecedentPersonnels, HttpStatus.OK); // Retourne 200 OK si trouvé
        } catch (JeuneNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retourne 404 Not Found si le jeune n'est pas trouvé
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Retourne 500 Internal Server Error en cas d'erreur serveur
        }
    }

    @PostMapping("/jeunes/confirm-Fisrtauth/{id}")
    public ResponseEntity<Map<String, String>> confirmAuthentification(@PathVariable Long id,@RequestBody Map<String, String> details) {
        try {
            String password=details.get("password");
            // Appeler le service pour confirmer l'authentification et obtenir le nouveau token
            Map<String, String> response = jeuneService.confirmAuthentification(id,password);

            // Retourner le token dans la réponse
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            // Retourner une réponse d'erreur si quelque chose échoue
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/jeunes/{id}/streams")
    public ResponseEntity<List<LiveDTO>> getAllLives(@PathVariable Long id, @PathParam(value = "phase") String phase){
		try {
			List<LiveDTO> L=null;
			if (phase.equals("question")) L = this.questionService.getonquestionsforuserId(id);
			else if (phase.equals("final")) L = this.questionService.getonfinalforuserId(id);
			if (L.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			return ResponseEntity.status(HttpStatus.OK).body(L);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
    }
    
    @PostMapping("/jeunes/{jeuneId}/streams/{liveId}/feedbacks")
    public ResponseEntity<String> createLive(@RequestBody LiveFeedbackDTO liveFeedback, @PathVariable("liveId") int liveId, @PathVariable("jeuneId") Long jeuneId) {
    	try {
        	this.liveFeedbackService.createFeedback(liveFeedback, liveId, jeuneId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Live feedback created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating Live feedback: "+e.getMessage());
        }
    }
    @GetMapping("/jeunes/{jeuneId}/streams/last")
    public ResponseEntity<LiveDTO> getSingleLive(@PathVariable Long jeuneId) {
    	try {
    		LiveDTO live = this.liveFeedbackService.getLastLiveUnanswered(jeuneId);
            if (live != null) {
                return ResponseEntity.status(HttpStatus.OK).body(live);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/jeune")
    public List<Jeune> getAllJeune() {
        return jeuneServiceImpl.getAllJeunes();
    }

    @GetMapping("/jeune/medecin/{medecinId}")
    public List<Object[]> getMedecinPatients(@PathVariable Long medecinId) {
        return jeuneServiceImpl.getMedecinPatients(medecinId);
    }

    @GetMapping("/jeune/dossier-medical/{id}")
    public Object getJeuneDossierMedical(@PathVariable Long id) {
        return jeuneServiceImpl.getJeuneDossierMedical(id);
    }

    @GetMapping("/jeune/{id}")
    public ResponseEntity<Jeune> getJeuneById3(@PathVariable Long id) {
        return jeuneServiceImpl.getJeuneById3(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/jeune/favorite/{medecinId}")
    public List<Object[]> getFavoritePatients(@PathVariable Long medecinId) {
        return jeuneServiceImpl.getFavoritePatients(medecinId);
    }

    @PutMapping("/jeune/favorite/{id}/{favorite}")
    public void updateFavoriteState(@PathVariable Long id, @PathVariable Boolean favorite) {
        jeuneServiceImpl.updateFavoriteState(id, favorite);
    }

    @DeleteMapping("/jeune/{id}")
    public ResponseEntity<Void> deleteJeune2(@PathVariable Long id) {
        jeuneServiceImpl.deleteJeune2(id);
        return ResponseEntity.noContent().build();
    }
}

package ma.inpt.esj.controllers;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import ma.inpt.esj.dto.ConsultationDTO;
import ma.inpt.esj.dto.JeuneDto;
import ma.inpt.esj.entities.*;
import ma.inpt.esj.exception.EmailNonValideException;
import ma.inpt.esj.exception.JeuneException;
import ma.inpt.esj.exception.JeuneNotFoundException;
import ma.inpt.esj.exception.PhoneNonValideException;
import ma.inpt.esj.services.JeuneService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.inpt.esj.services.JeuneServiceImpl;

@RestController
@AllArgsConstructor
public class JeuneController {

    private static final Logger logger = Logger.getLogger(JeuneController.class.getName());
    private JeuneService jeuneService;

    @GetMapping("/jeunes/{id}")
    public ResponseEntity<?> getJeuneById(@PathVariable(value = "id") Long id) {
        try {
            Object jeune = jeuneService.getJeuneById(id);
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
        return ResponseEntity.ok(savedJeune);

    }

    @PostMapping("/register/jeunes/nonscolarise")
    public ResponseEntity<JeuneDto> saveJeuneNonScolarise(@RequestBody JeuneNonScolarise jeuneNonScolarise) {
        try {
            JeuneDto savedJeune = jeuneService.saveJeune(jeuneNonScolarise);
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

    @PostMapping("/{id}/consultations")
    public ResponseEntity<Jeune> addConsultationToJeune(@PathVariable Long id,
                                                        @RequestBody ConsultationDTO consultationDTO) {
        Jeune jeune = jeuneService.addConsultationDTOToJeune(id, consultationDTO);
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
        return ResponseEntity.ok(jeune);
    }

    @PostMapping("/jeunes/{id}/antecedentsPersonnels")
    public ResponseEntity<Jeune> addAntecedentPersonnelToJeune(@PathVariable Long id,
                                                               @RequestBody AntecedentPersonnel antecedentPersonnel) {
        Jeune jeune = jeuneService.addAntecedentPersonnelToJeune(id, antecedentPersonnel);
        if (jeune == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jeune);
    }


    @GetMapping("/jeunes/order-by-age-asc")
    public List<Jeune> getAllJeunesOrderByAgeAsc() {
        return jeuneService.getAllJeunesOrderByAgeAsc();
    }

    @GetMapping("/jeunes/order-by-age-desc")
    public List<Jeune> getAllJeunesOrderByAgeDesc() {
        return jeuneService.getAllJeunesOrderByAgeDesc();
    }

    @GetMapping("/jeunes/order-by-nom")
    public List<Jeune> getAllJeunesOrderByNom() {
        return jeuneService.getAllJeunesOrderByNom();
    }

    @GetMapping("/jeunes/order-by-prenom")
    public List<Jeune> getAllJeunesOrderByPrenom() {
        return jeuneService.getAllJeunesOrderByPrenom();
    }

    @GetMapping("/jeunes/get-by-sexe/{sexe}")
    public List<Jeune> getAllJeunesBySexe(@PathVariable String sexe) {
        return jeuneService.getAllJeunesBySexe(sexe);
    }

    @GetMapping("/jeunes/get-by-nom/{nom}")
    public List<Jeune> getAllJeunesByNom(@PathVariable String nom) {
        return jeuneService.getAllJeunesByNom(nom);
    }

    /* @GetMapping("/jeunes/{medecinId}/patients")
    public List<Jeune> getJeunesByMedecinId(@PathVariable Long medecinId) {
        return jeuneService.getJeunesByMedecinI(medecinId);
    }
    */
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
}

package ma.inpt.esj.controllers;

import lombok.AllArgsConstructor;
import ma.inpt.esj.dto.ProfessionnelSanteDTO;
import ma.inpt.esj.entities.ProfessionnelSante;
import ma.inpt.esj.exception.ProfessionnelException;
import ma.inpt.esj.exception.ProfessionnelNotFoundException;
import ma.inpt.esj.services.ProfessionnelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class ProfessionnelController {

    private final ProfessionnelService professionnelService;

    @PostMapping("/register/professionnels")
    public ResponseEntity<?> createProfessionnel(@RequestBody ProfessionnelSante professionnel) {
        try {
            ProfessionnelSanteDTO responseDTO = professionnelService.saveProfessionnel(professionnel);
            return ResponseEntity.ok(responseDTO);
        } catch (ProfessionnelException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors du traitement de la requête");
        }
    }

    @DeleteMapping("/professionnels/{id}")
    public ResponseEntity<String> deleteProfessionnel(@PathVariable Long id) {
        try {
            professionnelService.deleteProfessionnel(id);
            return ResponseEntity.ok("Professionnel supprimé avec succès");
        } catch (ProfessionnelNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ProfessionnelException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/professionnels/{id}")
    public ResponseEntity<ProfessionnelSanteDTO> getProfessionnelById(@PathVariable Long id) {
        try {
            ProfessionnelSanteDTO professionnel = professionnelService.getProfessionnelById(id);
            return ResponseEntity.ok(professionnel);
        } catch (ProfessionnelNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/professionnels/{id}")
    public ResponseEntity<ProfessionnelSanteDTO> patchProfessionnel(@PathVariable Long id, @RequestBody Map<String, Object> updates) throws ProfessionnelNotFoundException {
        ProfessionnelSanteDTO updatedProfessionnel = professionnelService.updateProfessionnel(id, updates);
        return ResponseEntity.ok(updatedProfessionnel);
    }

    @GetMapping("/professionnels")
    public ResponseEntity<List<ProfessionnelSanteDTO>> getAllProfessionnels() {
        List<ProfessionnelSanteDTO> professionnels = professionnelService.getAllProfessionnels();
        return ResponseEntity.ok(professionnels);
    }

    @ExceptionHandler(ProfessionnelException.class)
    public ResponseEntity<Object> handleProfessionnelException(ProfessionnelException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

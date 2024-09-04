package ma.inpt.esj.controllers;

import lombok.AllArgsConstructor;
import ma.inpt.esj.dto.ProfessionnelSanteDTO;
import ma.inpt.esj.dto.ProfessionnelSanteRequestDTO;
import ma.inpt.esj.entities.ProfessionnelSante;
import ma.inpt.esj.exception.ProfessionnelException;
import ma.inpt.esj.exception.ProfessionnelNotFoundException;
import ma.inpt.esj.mappers.ProfessionnelMapper;
import ma.inpt.esj.services.ProfessionnelService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class ProfessionnelController {

    private final ProfessionnelService professionnelService;
    private final ProfessionnelMapper professionnelMapper;
    @PostMapping("/register/professionnels")
    public ResponseEntity<?> createProfessionnel(@RequestBody ProfessionnelSanteRequestDTO professionnelSanteRequestDTO) {
        try {
            ProfessionnelSante professionnelSante = professionnelMapper.fromProfessionnelDTO(professionnelSanteRequestDTO);
            ProfessionnelSanteDTO responseDTO = professionnelService.saveProfessionnel(professionnelSante);
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

    @PostMapping("/professionnels/confirm-Fisrtauth/{id}")
    public ResponseEntity<Map<String, String>> confirmAuthentification(@PathVariable Long id,@RequestBody Map<String, String> details) {
        try {
            String password=details.get("password");
            // Appeler le service pour confirmer l'authentification et obtenir le nouveau token
            Map<String, String> response = professionnelService.confirmAuthentification(id,password);

            // Retourner le token dans la réponse
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            // Retourner une réponse d'erreur si quelque chose échoue
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

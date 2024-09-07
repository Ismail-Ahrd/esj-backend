package ma.inpt.esj.controllers;

import lombok.AllArgsConstructor;
import ma.inpt.esj.repositories.InfoUserRepository;
import ma.inpt.esj.repositories.JeuneRepository;
import ma.inpt.esj.repositories.MedecinRepository;
import ma.inpt.esj.repositories.ProfessionnelRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/validator")
@AllArgsConstructor
public class ValidatorController {

    private final InfoUserRepository infoUserRepository;
    private final JeuneRepository jeuneRepository;
    private final MedecinRepository medecinRepository;
    private final ProfessionnelRepository professionnelSanteRepository;


    @GetMapping("/infouser")
    public ResponseEntity<String> validateInfoUser(
            @RequestParam(required = false) String mail,
            @RequestParam(required = false) String numTel) {

        boolean mailExists = mail != null && infoUserRepository.existsByMail(mail);
        boolean numTelExists = numTel != null && infoUserRepository.existsByNumTel(numTel);

        if (mailExists || numTelExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Mail or numTel already exists");
        } else {
            return ResponseEntity.ok("OK");
        }
    }

    @GetMapping("/mail")
    public ResponseEntity<String> valideEmail(@RequestParam String mail){
        boolean mailExiste=infoUserRepository.existsByMail(mail);
        if (mailExiste){
            return ResponseEntity.ok("OK");

        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("mail already exists");
        }
    }

    @GetMapping("/cin")
    public ResponseEntity<String> validateCin(@RequestParam String cin) {
        boolean existsInJeune = jeuneRepository.existsByCin(cin);
        boolean existsInMedecin = medecinRepository.existsByCin(cin);
        boolean existsInProfessionnelSante = professionnelSanteRepository.existsByCin(cin);

        if (!existsInJeune && !existsInMedecin && !existsInProfessionnelSante) {
            return ResponseEntity.ok("OK");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CIN already exists");
        }
    }

    @GetMapping("/inpe")
    public ResponseEntity<String> validateInpe(@RequestParam String inpe) {
        boolean existsInMedecin = medecinRepository.existsByInpe(inpe);
        boolean existsInProfessionnelSante = professionnelSanteRepository.existsByInpe(inpe);

        if (existsInMedecin || existsInProfessionnelSante) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("INPE already exists");
        } else {
            return ResponseEntity.ok("OK");
        }
    }

    @GetMapping("/ppr")
    public ResponseEntity<String> validatePpr(@RequestParam(required = false) String ppr) {
        if (ppr == null || ppr.isEmpty()) {
            return ResponseEntity.badRequest().body("PPR is required");
        }

        boolean existsInMedecin = medecinRepository.existsByPpr(ppr);

        if (existsInMedecin) {
            return ResponseEntity.ok("PPR already exists");
        } else {
            return ResponseEntity.ok("OK");
        }
    }
    @GetMapping("/cne")
    public ResponseEntity<String> validateCne(@RequestParam String cne) {
        boolean existsInJeuneCne1 = jeuneRepository.existsByNSCNE(cne);
        boolean existsInJeuneCne2 = jeuneRepository.existsBySCNE(cne);

        if (existsInJeuneCne1 || existsInJeuneCne2) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CNE already exists");
        } else {
            return ResponseEntity.ok("OK");
        }
    }

    @GetMapping("/codeMassare")
    public ResponseEntity<String> validateCodeMassare(@RequestParam String codeMassare) {

        boolean existsInJeuneCodeMassare1= jeuneRepository.existsByNSCodeMassare(codeMassare);
        boolean existsInJeuneCodeMassare2 = jeuneRepository.existsBySCodeMassare(codeMassare);

        if (existsInJeuneCodeMassare1 || existsInJeuneCodeMassare2) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Code Massare already exists");
        } else {
            return ResponseEntity.ok("OK");
        }
    }

}

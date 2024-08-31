package ma.inpt.esj.controllers;


import lombok.AllArgsConstructor;
import ma.inpt.esj.entities.Administrateur;
import ma.inpt.esj.entities.Jeune;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.entities.ProfessionnelSante;
import ma.inpt.esj.exception.*;
import ma.inpt.esj.services.ConfirmeMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@AllArgsConstructor
public class ConfirmationMailController {
    @Autowired
    private ConfirmeMailService confirmeMailService;

    @GetMapping("/register/confirmation")
    public RedirectView confirmEmail(@RequestParam("token") String token) throws ConfirmationMailException {
        Object person = confirmeMailService.confirmEmail(token);

        if (person instanceof Medecin) {
            return new RedirectView("http://localhost:3000/auth/medecins");
        } else if (person instanceof ProfessionnelSante) {
            return new RedirectView("http://localhost:3000/auth/professionnels");

        } else if (person instanceof Jeune) {
            return new RedirectView("http://localhost:3000/auth/jeunes");
        } else if (person instanceof Administrateur) {
            return new RedirectView("http://localhost:3000/auth/administrateur");
        } else {
            throw new ConfirmationMailException("Unknown person type");
        }
    }

    @PostMapping("/register/resend-token")
    public ResponseEntity<String> resendToken(@RequestParam("email") String email) throws JeuneNotFoundException, UserNotFoundException, MedecinNotFoundException, ProfessionnelNotFoundException,AdministrateurNotFoundException {
        System.out.println("***************************************************");
        confirmeMailService.resendToken(email);
        return ResponseEntity.ok("Token resent successfully");

    }

    @ExceptionHandler(ConfirmationMailException.class)
    public ResponseEntity<String> handleConfirmationMailException(ConfirmationMailException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleTokenExpiredException(TokenExpiredException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.GONE);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

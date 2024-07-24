package ma.inpt.esj.controllers;

import ma.inpt.esj.exception.CompteRenduException;
import ma.inpt.esj.exception.MedecinException;
import ma.inpt.esj.exception.MedecinNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;
import ma.inpt.esj.exception.CompteRenduNotFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceprtionHandler {
    @ExceptionHandler(MedecinNotFoundException.class)
    public ResponseEntity<String> handleMedecinNotFoundException(MedecinNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MedecinException.class)
    public ResponseEntity<String> handleMedecinException(MedecinException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CompteRenduNotFoundException.class)
    public ResponseEntity<String> handleCompteRenduNotFoundException(CompteRenduNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CompteRenduException.class)
    public ResponseEntity<String> handleCompteRenduException(CompteRenduException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
        log.info(ex.getMessage());
        return new ResponseEntity<>("Une erreur interne s'est produite. Veuillez réessayer plus tard.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

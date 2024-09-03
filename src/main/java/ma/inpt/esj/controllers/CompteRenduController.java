package ma.inpt.esj.controllers;

import jakarta.validation.Valid;
import ma.inpt.esj.dto.CompteRenduDto;
import ma.inpt.esj.exception.CompteRenduException;
import ma.inpt.esj.exception.CompteRenduNotFoundException;
import ma.inpt.esj.exception.DiscussionNotFoundException;
import ma.inpt.esj.services.CompteRenduService;
import ma.inpt.esj.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compterendu")
public class CompteRenduController {

    private final CompteRenduService compteRenduService;
    private final JwtUtil jwtUtil;

    @Autowired
    public CompteRenduController(CompteRenduService compteRenduService, JwtUtil jwtUtil) {
        this.compteRenduService = compteRenduService;
        this.jwtUtil = jwtUtil;
    }

    /* @GetMapping
    public ResponseEntity<?> getAllComptesRendus() {
        try {
            Iterable<CompteRendu> comptesRendus = compteRenduService.getAllComptesRendus();
            return ResponseEntity.ok(comptesRendus);
        } catch (CompteRenduException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    } */

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompteRenduById(@PathVariable Long id) {
        Long userId = jwtUtil.getUserIdFromJwt();
        try {
            CompteRenduDto compteRendu = compteRenduService.getCompteRenduById(id, userId);
            return ResponseEntity.ok(compteRendu);
        } catch (CompteRenduNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DiscussionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (CompteRenduException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createCompteRendu(@Valid @RequestBody CompteRenduDto compteRendu) {
        Long userId = jwtUtil.getUserIdFromJwt();
        try {
            CompteRenduDto c = compteRenduService.createCompteRendu(userId, compteRendu);
            return ResponseEntity.status(HttpStatus.CREATED).body(c);
        } catch (CompteRenduException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DiscussionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /* @PutMapping("/{id}")
    public ResponseEntity<?> updateCompteRendu(@PathVariable Long id, @Valid @RequestBody CompteRendu compteRendu) {
        try {
            CompteRendu c = compteRenduService.updateCompteRendu(id, compteRendu);
            return ResponseEntity.ok(c);
        } catch (CompteRenduNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (CompteRenduException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    } */
}

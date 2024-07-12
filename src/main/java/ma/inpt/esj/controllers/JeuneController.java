package ma.inpt.esj.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.inpt.esj.entities.Jeune;
import ma.inpt.esj.services.JeuneService;

@RestController
@RequestMapping("/api/jeune")
public class JeuneController {
    @Autowired
    private JeuneService jeuneService;

    @GetMapping
    public List<Jeune> getAllJeune() {
        return jeuneService.getAllJeunes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jeune> getJeuneById(@PathVariable Long id) {
        return jeuneService.getJeuneById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Jeune createJeune(@RequestBody Jeune jeune) {
        return jeuneService.createJeune(jeune);
    }

    @PutMapping
    public ResponseEntity<Jeune> updateJeune(@PathVariable Long id, @RequestBody Jeune jeuneDetails) {
        return ResponseEntity.ok(jeuneService.updateJeune(id, jeuneDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJeune(@PathVariable Long id) {
        jeuneService.deleteJeune(id);
        return ResponseEntity.noContent().build();
    }
}

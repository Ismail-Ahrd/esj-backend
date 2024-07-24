package ma.inpt.esj.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.inpt.esj.entities.Jeune;
import ma.inpt.esj.repositories.JeuneRepository;

@Service
public class JeuneService {
    @Autowired
    private JeuneRepository jeuneRepository;

    public List<Jeune> getAllJeunes() {
        return jeuneRepository.findAll();
    }

    public Optional<Jeune> getJeuneById(Long id) {
        return jeuneRepository.findById(id);
    }

    public Jeune createJeune(Jeune jeune) {
        return jeuneRepository.save(jeune);
    }

    public Jeune updateJeune(Long id, Jeune jeuneDetails) {
        Jeune jeune = jeuneRepository.findById(id).orElseThrow(() -> new RuntimeException("Jeune not found"));
        jeune.setSexe(jeuneDetails.getSexe());
        jeune.setDateNaissance(jeuneDetails.getDateNaissance());
        jeune.setAge(jeuneDetails.getAge());
        jeune.setIdentifiantPatient(jeuneDetails.getIdentifiantPatient());
        jeune.setScolarise(jeuneDetails.isScolarise());
        jeune.setFavorite(jeuneDetails.isFavorite());
        return jeuneRepository.save(jeune);
    }

    public void deleteJeune(Long id) {
        jeuneRepository.deleteById(id);
    }

    public List<Jeune> getAllJeunesOrderByAgeAsc() {
        return jeuneRepository.getAllJeunesOrderByAgeAsc();
    }

    public List<Jeune> getAllJeunesOrderByAgeDesc() {
        return jeuneRepository.getAllJeunesOrderByAgeDesc();
    }

    public List<Jeune> getAllJeunesOrderByNom() {
        return jeuneRepository.getAllJeunesOrderByNom();
    }

    public List<Jeune> getAllJeunesOrderByPrenom() {
        return jeuneRepository.getAllJeunesOrderByPrenom();
    }

    /*
    public List<Consultation> getAllConsultationByDateAsc(Long id) {
        return jeuneRepository.getAllConsultationByDateAsc(id);
    }

    public List<Consultation> getAllConsultationByDateDesc(Long id) {
        return jeuneRepository.getAllConsultationByDateDesc(id);
    }
    */

    public List<Jeune> getAllJeunesBySexe(String sexe) {
        return jeuneRepository.getAllJeunesBySexe(sexe);
    }

    public List<Jeune> getAllJeunesByNom(String nom) {
        return jeuneRepository.getAllJeunesByNom(nom);
    }

    /*
    public List<Jeune> getAllJeunesByMaladie(String maladie) {
        return jeuneRepository.getAllJeunesByMaladie(maladie);
    }
    */

    public List<Jeune> getJeunesByMedecinId(Long medecinId) {
        return jeuneRepository.findByMedecinId(medecinId);
    }
}

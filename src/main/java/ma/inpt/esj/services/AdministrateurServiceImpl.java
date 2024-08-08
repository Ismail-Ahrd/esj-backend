package ma.inpt.esj.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.inpt.esj.exception.AdministrateurNotFoundException;
import ma.inpt.esj.dto.AdministrateurDTO;
import ma.inpt.esj.entities.Administrateur;
import ma.inpt.esj.entities.InfoUser;
import ma.inpt.esj.repositories.AdministrateurRepository;
import ma.inpt.esj.repositories.InfoUserRepository;

@Service
public class AdministrateurServiceImpl implements AdministrateurService{
    @Autowired
    AdministrateurRepository administrateurRepository;
    @Autowired
    private InfoUserRepository infoUserRepository;
    
    public List<Administrateur> getAllAdmin(){
        return this.administrateurRepository.findAll();
    }
    public Optional<Administrateur> getSingleone(Long id){
        return this.administrateurRepository.findById(id);
    }
    public void createOne(Administrateur ad){
    	InfoUser infoUser = infoUserRepository.save(ad.getInfoUser());

        ad.setInfoUser(infoUser);
        administrateurRepository.save(ad);
    }
    
    public void updateOne(AdministrateurDTO updatedAdministrateur) throws AdministrateurNotFoundException{
        // Vérifiez si l'administrateur existe
        if (!administrateurRepository.existsById(updatedAdministrateur.getId())) throw new AdministrateurNotFoundException("Administrateur avec ID " + updatedAdministrateur.getId() + " est introuvable.");
        if (!infoUserRepository.existsById(updatedAdministrateur.getInfoUser().getId())) throw new AdministrateurNotFoundException("InfoUser avec ID " + updatedAdministrateur.getId() + " est introuvable.");
        // Sauvegardez l'administrateur mis à jour
        infoUserRepository.save(updatedAdministrateur.getInfoUser());
    }
    
    public void deleteOne(Long id) throws AdministrateurNotFoundException{
        if (!administrateurRepository.existsById(id)) throw new AdministrateurNotFoundException("Administrateur avec ID " + id + " est introuvable.");
        this.administrateurRepository.deleteById(id);
    }

    /*public ResponseEntity Authenticate(Administrateur ad){
        String email =ad.getEmail();
        String mdp=ad.getMdp();
        if(this.administrateurRepository.findByEmail(email).isPresent() && this.administrateurRepository.findByMdp(mdp).isPresent()){
            return ResponseEntity.ok(ad);

        }
        else{
            return ResponseEntity.status(404).body(new String("ADMIN NOT FOUND"));
        }
    }*/
}

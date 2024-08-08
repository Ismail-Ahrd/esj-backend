package ma.inpt.esj.services;

import java.util.List;
import java.util.Optional;

import ma.inpt.esj.exception.AdministrateurNotFoundException;
import ma.inpt.esj.dto.AdministrateurDTO;
import ma.inpt.esj.entities.Administrateur;


public interface AdministrateurService {
    
    public List<Administrateur> getAllAdmin();
    
    public Optional<Administrateur> getSingleone(Long id);
    
    public void createOne(Administrateur ad);
    
    public void updateOne(AdministrateurDTO updatedAdministrateur) throws AdministrateurNotFoundException;
    
    public void deleteOne(Long id) throws AdministrateurNotFoundException;

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

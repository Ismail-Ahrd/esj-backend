package ma.inpt.esj.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ma.inpt.esj.exception.AdminException;
import ma.inpt.esj.exception.AdministrateurNotFoundException;
import ma.inpt.esj.dto.AdministrateurDTO;
import ma.inpt.esj.entities.Administrateur;
import org.apache.coyote.BadRequestException;


public interface AdministrateurService {
    
    public List<Administrateur> getAllAdmin();
    
    public Optional<Administrateur> getSingleone(Long id);
    
    public void createOne(Administrateur ad);
    
    public void updateOne(AdministrateurDTO updatedAdministrateur) throws AdministrateurNotFoundException;
    
    public void deleteOne(Long id) throws AdministrateurNotFoundException;

    AdministrateurDTO saveAdmin(Administrateur administrateur) throws AdminException;

    Map<String, String> confirmAuthentification(Long id, String password) throws BadRequestException;
 }

package ma.inpt.esj.services;

import java.util.List;

import ma.inpt.esj.dto.ResponsableDTO;
import ma.inpt.esj.entities.Responsable;

public interface ResponsableService {
    
    public List<ResponsableDTO> getAll();
    
    public ResponsableDTO getSingleOne(Long id);
    
    public void createOne(Responsable R);
    
    public void updateOne(Responsable R);

    public void deleteOne(Long id);
}

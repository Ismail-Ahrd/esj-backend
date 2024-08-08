package ma.inpt.esj.services;

import java.util.List;

import ma.inpt.esj.exception.ThemeNotFoundException;
import ma.inpt.esj.dto.ThemeDTO;

public interface ThemeService {
    public List<ThemeDTO> getAll();
    
    public ThemeDTO getSingle(int id);
    
    public void createOne(ThemeDTO T);
    
    public void deleteOne(int id) throws ThemeNotFoundException;
    
    public void updateOne(ThemeDTO T, int id) throws ThemeNotFoundException;
}

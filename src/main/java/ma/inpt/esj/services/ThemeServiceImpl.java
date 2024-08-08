package ma.inpt.esj.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.inpt.esj.exception.ThemeNotFoundException;
import ma.inpt.esj.dto.ThemeDTO;
import ma.inpt.esj.entities.Theme;
import ma.inpt.esj.mappers.ThemeMapper;
import ma.inpt.esj.repositories.ThemeRepository;

@Service
public class ThemeServiceImpl implements ThemeService {
    @Autowired
    ThemeRepository themeRepository;
    @Autowired
    ThemeMapper themeMapper;
    public List<ThemeDTO> getAll(){



        List<Theme> L=this.themeRepository.findAll();
        return this.themeMapper.getallDto(L);
    }
   /* public List<DTOTHEMATIQUE> getAllByIdLiver(int id){



        List<PropositionThématique> L=this.Cruder.findByLiveId(id);
        return this.M.getallDto(L);
    }*/
    public ThemeDTO getSingle(int id){
        Theme P=this.themeRepository.findById(id).get();
        return this.themeMapper.getSingle(P);
    }
    public void createOne(ThemeDTO T){
        Theme P=this.themeMapper.Creating(T);
        this.themeRepository.save(P);
    }
    public void deleteOne(int id) throws ThemeNotFoundException{
    	themeRepository.findById(id)
    	.orElseThrow(() -> new ThemeNotFoundException("Le theme d'id "+id+" est introuvable."));
        this.themeRepository.deleteById(id);
    }
    public void updateOne(ThemeDTO T, int id) throws ThemeNotFoundException{
    	themeRepository.findById(id)
    	.orElseThrow(() -> new ThemeNotFoundException("Le theme d'id "+id+" est introuvable."));
        Theme P=this.themeMapper.Creating(T);
        P.setId(id);
        this.themeRepository.save(P);    }
}

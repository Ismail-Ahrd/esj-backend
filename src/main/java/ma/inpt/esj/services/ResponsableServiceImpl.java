package ma.inpt.esj.services;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.inpt.esj.dto.ResponsableDTO;
import ma.inpt.esj.entities.Responsable;
import ma.inpt.esj.mappers.ResponsableMapper;
import ma.inpt.esj.repositories.ResponsableRepository;

@Service
public class ResponsableServiceImpl implements ResponsableService {
    @Autowired
    ResponsableRepository responsableRepository;
    @Autowired
    ResponsableMapper Mapper;
    
    public List<ResponsableDTO> getAll(){

        List<Responsable> L=this.responsableRepository.findAll();
        List<ResponsableDTO> L2=new ArrayList<>();
        for(int i=0;i<L.size();i++){
            L2.add(this.Mapper.fromResponsable(L.get(i)));
        }
        return  L2;
    }
    public ResponsableDTO getSingleOne(Long id){

        return this.Mapper.fromResponsable(this.responsableRepository.findById(id).get());
    }
    public void createOne(Responsable R){

        this.responsableRepository.save(R);
    }
    public void updateOne(Responsable R){
        this.responsableRepository.save(R);
    }

    public void deleteOne(Long id){
        this.responsableRepository.deleteById(id);
    }
}

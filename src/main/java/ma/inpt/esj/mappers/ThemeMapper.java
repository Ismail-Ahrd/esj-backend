package ma.inpt.esj.mappers;

import org.springframework.stereotype.Service;

import ma.inpt.esj.dto.ThemeDTO;
import ma.inpt.esj.entities.Theme;

import java.util.ArrayList;
import java.util.List;
@Service
public class ThemeMapper {
    public  List<ThemeDTO> getallDto(List<Theme> L){
        List<ThemeDTO> LD=new ArrayList<>();
        for(int i=0;i<L.size();i++){
            ThemeDTO P=new ThemeDTO();
            P.setId(L.get(i).getId());
            P.setContenu(L.get(i).getContenu());
            //P.setJeunes(L.get(i).getJeunes());
            LD.add(P);
        }
        return LD;
    }

    public   ThemeDTO getSingle(Theme T){
        ThemeDTO P=new ThemeDTO();
        P.setId(T.getId());
        P.setContenu(T.getContenu());
        //P.setJeunes(T.getJeunes());
        return P;
    }
    public List<Theme> Inverse(List<ThemeDTO> P){
        List<Theme> LD=new ArrayList<>();
        for(int i=0;i<P.size();i++){
            Theme TE=new Theme();
            TE.setId(P.get(i).getId());
            TE.setContenu(P.get(i).getContenu());
            //TE.setJeunes(P.get(i).getJeunes());
            LD.add(TE);
        }
        return LD;
    }
    public Theme Iversing(ThemeDTO P){
        Theme K=new Theme();
        K.setContenu(P.getContenu());
        return K;
    }
    public  Theme  Creating(ThemeDTO P){
        Theme T=new Theme();
        T.setContenu(P.getContenu());
        //T.setJeunes(P.getJeunes());
        return T;
    }
    public  Theme Updating(ThemeDTO P){
        Theme T=new Theme();
        T.setId(P.getId());
        T.setContenu(P.getContenu());
        //T.setJeunes(P.getJeunes());
        return T;
    }
}

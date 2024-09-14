package ma.inpt.esj.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AntecedentPersonnel {

    private List<String> maladies=new ArrayList<>();

    private Boolean utiliseMedicaments=false;

    private List<String> medicaments = new ArrayList<>();

    private Boolean chirurgicaux = false ;

    private OperationChirurgicale operationsChirurgicales;

    private List<String> habitudes = new ArrayList<>();

    private Integer cigarettesParJour = 0;

    private String consommationAlcool;

    private String tempsEcran;

    private String dureeFumee;
    //---------------------------------------------

    private String type;
    private String specification;
    private String specificationAutre;
    private int nombreAnnee ;
}


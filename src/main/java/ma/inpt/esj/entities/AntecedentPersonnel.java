package ma.inpt.esj.entities;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AntecedentPersonnel {


    private List<String> maladies;

    private Boolean utiliseMedicaments;


    private List<String> medicaments;

    private Boolean chirurgicaux;

    private OperationChirurgicale operationsChirurgicales;


    private List<String> habitudes;

    private Integer cigarettesParJour;

    private String consommationAlcool;

    private String tempsEcran;

    private String dureeFumee;
    //---------------------------------------------

    private String type;
    private String specification;
    private String specificationAutre;
    private int nombreAnnee ;
}


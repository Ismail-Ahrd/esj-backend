package ma.inpt.esj.entities;


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
public class AntecedentFamilial {

    private List<String> maladiesFamiliales;

    //----------------------------
    private String typeAntFam;
    private String autre;
}



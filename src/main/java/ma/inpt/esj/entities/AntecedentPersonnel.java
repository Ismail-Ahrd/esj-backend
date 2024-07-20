package ma.inpt.esj.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class AntecedentPersonnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "jeune_id")
    private Jeune jeune;

    @ElementCollection
    private List<String> maladies;

    private Boolean utiliseMedicaments;

    @ElementCollection
    private List<String> medicaments;

    private Boolean chirurgicaux;

    private OperationChirurgicale operationsChirurgicales;

    @ElementCollection
    private List<String> habitudes;

    private Integer cigarettesParJour;

    private String consommationAlcool;

    private String tempsEcran;
}

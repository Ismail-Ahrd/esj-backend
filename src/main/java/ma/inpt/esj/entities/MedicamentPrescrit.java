package ma.inpt.esj.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class MedicamentPrescrit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int dose;
    private String instructions;
    private int nombre_de_prises_par_jour;
}

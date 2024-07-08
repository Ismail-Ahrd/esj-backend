package ma.inpt.esj.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class FicheReference {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String qrCode;
    @ElementCollection
    private List<String> coordonneesSoins;
    @OneToOne(cascade = CascadeType.ALL)
    private TestPsychologique testPsychologique;
}

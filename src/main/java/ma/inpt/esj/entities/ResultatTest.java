package ma.inpt.esj.entities;

import java.sql.Date;

import jakarta.persistence.CascadeType;
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
public class ResultatTest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int score;
    private Date date;
    private String interpretation;
    @OneToOne(cascade = CascadeType.ALL)
    private Jeune jeune;
    @OneToOne(cascade = CascadeType.ALL)
    private TestPsychologique testPsychologique;
}

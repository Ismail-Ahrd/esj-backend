package ma.inpt.esj.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class Medecin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String cin;
    @Column(unique = true)
    private String inpe;
    @Column(unique = true)
    private String ppr;
    private boolean estMedcinESJ;
    private boolean estGeneraliste;
    private String specialite;
    @OneToOne(cascade = CascadeType.ALL)
    private InfoUser infoUser;

    private String ROLE="MEDECIN";

    private boolean confirmed =false;

    private boolean isFirstAuth=true;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Jeune> patients;
}

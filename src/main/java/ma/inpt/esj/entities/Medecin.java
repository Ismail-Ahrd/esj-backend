package ma.inpt.esj.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.inpt.esj.entities.Education;
import ma.inpt.esj.entities.Experience;

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
@Column(columnDefinition = "TEXT") // This annotation allows for a larger string
    private String about;
            private String sexe;

    private boolean estMedcinESJ;
    private boolean estGeneraliste;
    private String specialite;

    @OneToOne(cascade = CascadeType.ALL)
    private InfoUser infoUser;
            private String linkedin;
            private String twitter;
@OneToMany(cascade = CascadeType.ALL)
    private List<Education> education;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Experience> experience;
    private String ROLE="MEDECIN";

    private boolean confirmed =false;

    private boolean isFirstAuth=true;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Jeune> patients;
}

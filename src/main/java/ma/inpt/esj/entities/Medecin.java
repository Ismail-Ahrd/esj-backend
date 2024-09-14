package ma.inpt.esj.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Medecin extends Responsable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String cin;

    @Column(unique = true)
    private String inpe;

    @Column(unique = true)
    private String ppr;

    private String sexe;

    private String linkedin;

    private boolean estMedcinESJ;

    private boolean estGeneraliste;

    private String specialite;
        
    @OneToOne(cascade = CascadeType.ALL)
    private InfoUser infoUser;

    private String ROLE="MEDECIN";

    @Column(columnDefinition = "TEXT")
    private String about;

    @ElementCollection
    @CollectionTable(name = "langues_parlees", joinColumns = @JoinColumn(name = "medecin_id"))
    @Column(name = "langue")
    private List<String> languesParlees;
    
    @ElementCollection
    @CollectionTable(name = "specialites_medicales", joinColumns = @JoinColumn(name = "medecin_id"))
    @Column(name = "specialite")
    private List<String> specialites;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "medecin_id")
    private List<Education> educations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "medecin_id")
    private List<Experience> experiences = new ArrayList<>();

    @Column(name = "evaluation", columnDefinition = "INTEGER DEFAULT 0")
    private Integer evaluation;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Consultation> consultations;
    
    @Override
    public String isRole() {
    	return this.specialite;
    }

    @Override
    public InfoUser hasInfoUser() {
        return this.infoUser;
    }
}

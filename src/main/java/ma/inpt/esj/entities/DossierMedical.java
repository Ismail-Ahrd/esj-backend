package ma.inpt.esj.entities;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class DossierMedical {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Consultation> historiqueConsultations;
    @ElementCollection
    private List<String> maladiesDiagnostiquees;
    @OneToMany(cascade = CascadeType.ALL)
    private List<MedicamentPrescrit> medicamentsPrescrits;
    @OneToOne(cascade = CascadeType.ALL)
    private AntecedentFamilial antecedentsFamiliaux;
    @OneToOne(cascade = CascadeType.ALL)
    private AntecedentPersonnel antecedentPersonnel;
    @ElementCollection
    private List<String> allergies;
    @OneToMany(cascade = CascadeType.ALL)
    private List<PriseEnCharge> priseEnCharges;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Correspondance> correspondances;
    @OneToOne(cascade = CascadeType.ALL)
    private Jeune jeune;
}

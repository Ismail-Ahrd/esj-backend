package ma.inpt.esj.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;


    @OneToOne(targetEntity = Medecin.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "medecin_id")
    private Medecin medecin;

    @OneToOne(targetEntity = ProfessionnelSante.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "professionnelSante_id")
    private ProfessionnelSante professionnelSante;

    @OneToOne(targetEntity = Jeune.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "jeune_id")
    private Jeune jeune;


    @OneToOne(targetEntity = Administrateur.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name ="admin_id")
    private Administrateur admin;


    private Date createdDate;

}

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

    private Date createdDate;

}

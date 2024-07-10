package ma.inpt.esj.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class InfoUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String prenom;

    private String numTel;

    @Column(unique = true)
    private String mail;

    private String motDePasse;
}

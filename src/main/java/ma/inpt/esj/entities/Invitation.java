package ma.inpt.esj.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ma.inpt.esj.enums.InvitationStatus;
import lombok.Builder;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class Invitation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    private Medecin medecinInvite;

    @ManyToOne(cascade = CascadeType.ALL)
    private Discussion discussion;
}

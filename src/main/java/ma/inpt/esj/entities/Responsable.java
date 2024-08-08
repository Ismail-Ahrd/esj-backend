package ma.inpt.esj.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Responsable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "responsable_id")
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    private InfoUser infoUser;
    @OneToMany(mappedBy = "responsable")
    @JsonIgnore
    private List<Live> lives;
    
    public abstract String isRole();
}

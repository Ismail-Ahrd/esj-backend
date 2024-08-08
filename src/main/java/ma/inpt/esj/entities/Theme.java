package ma.inpt.esj.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Theme {
    @Id
    @SequenceGenerator(name="generated",sequenceName ="generated",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "generated")
    @Column(name = "id_thematique")
    private int id;
    @Column(name="thematique",columnDefinition = "TEXT",nullable = false)
    String contenu;

   /* @OneToMany(mappedBy = "Thematique_Choisit")
    List<Jeunes> Jeunes;*/

    //@ManyToOne(fetch = FetchType.LAZY)


    //@JoinColumn(name = "id_Live")
    @OneToMany(mappedBy="thematique")
    List<Live> lives;

    public Theme(int id, String contenu) {
        this.id = id;
        this.contenu = contenu;
    }
}

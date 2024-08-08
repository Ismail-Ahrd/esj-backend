package ma.inpt.esj.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @SequenceGenerator(name="generated",sequenceName ="generated",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "generated")
    @Column(name = "id_question")
    private  int id;
    @Column(name="Contenu",columnDefinition = "TEXT",nullable = false)
    String contenu;

    /*@ManyToOne
    @JoinColumn(name="id_Jeune")
    private Jeunes jeune;*/

    @ManyToOne
    @JoinColumn(name="id_Live")
    @JsonIgnore
    private  Live live;
    
    @ManyToOne
    @JoinColumn(name="jeuneId")
    @JsonIgnore
    private Jeune jeune;

    public Question(int id, String contenu) {
        this.id = id;
        this.contenu = contenu;
    }
}

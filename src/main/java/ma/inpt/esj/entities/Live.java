package ma.inpt.esj.entities;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Live {
    @Id
    @SequenceGenerator(name="generated",sequenceName ="generated",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "generated")
    @Column(name = "id_Live")
    private int id;

    @Column(name = "Sujet",columnDefinition = "TEXT",nullable = false)
    private String subject;
    @Column(name = "Date",columnDefinition = "TEXT",nullable = false)
    private LocalDateTime date;
    @Column(name = "StreamYard",columnDefinition = "TEXT",nullable = false)
    private String lienStreamYard;
    @Column(name = "Lien_Youtube",columnDefinition = "TEXT",nullable = false)

    private String lienYoutube;
    
	private int img;
    
    private boolean active=false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="responsable_id")
    private Responsable responsable;

   /* @JoinColumn(name="id_Responsable")

    @JsonProperty("responsable")

    private Responsable responsable;*/

     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_admin")

    private Administrateur admin;

    @OneToMany(mappedBy = "live")
    private List<Question> questionsList;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "id_thematique")
    @JsonProperty("thématique")
    private Theme thematique;

    @OneToMany(mappedBy = "live")
    private List<LiveFeedback> feedbacks;

    public Live(int id,String subject,LocalDateTime date,String l1,String l2){
        this.id=id;
       this.subject=subject;
        this.date=date;
        this.lienStreamYard=l1;
        this.lienYoutube=l2;

    }
}

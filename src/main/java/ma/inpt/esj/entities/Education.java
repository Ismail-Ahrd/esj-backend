package ma.inpt.esj.entities;

<<<<<<< HEAD

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class Education {
     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String year; // or Long if you prefer numeric years
    private String diploma;
    private String institut;
 
    public Education(String year, String diploma, String institut) {
        this.year = year;
        this.diploma = diploma;
        this.institut = institut;
    }
}

=======
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class Education {
    private String annee;
    private String diplome;
    private String institut;
}
>>>>>>> 820ca28bec87b707ea2d374c0531c03672c815bc

package ma.inpt.esj.entities;

<<<<<<< HEAD

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class Experience {
     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String year; // or Long if you prefer numeric years
    private String position;
    private String hospital;
 

    public Experience(String year, String position, String hospital) {
        this.year = year;
        this.position = position;
        this.hospital = hospital;
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
public class Experience {
    private String annee;
    private String poste;
    private String hopital;
}
>>>>>>> 820ca28bec87b707ea2d374c0531c03672c815bc

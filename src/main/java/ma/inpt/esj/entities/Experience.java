package ma.inpt.esj.entities;


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


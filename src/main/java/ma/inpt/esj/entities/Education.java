package ma.inpt.esj.entities;


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


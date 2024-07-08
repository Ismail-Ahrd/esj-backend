package ma.inpt.esj.entities;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @Builder @ToString
public class Administrateur extends InfoUser{
    
}

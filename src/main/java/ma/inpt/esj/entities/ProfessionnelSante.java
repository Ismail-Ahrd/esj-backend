package ma.inpt.esj.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor @ToString
public class ProfessionnelSante extends Responsable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cin;
    private String inpe;
    @OneToOne(cascade = CascadeType.ALL)
    private InfoUser infoUser;
    private String ROLE="PROFESSIONELSANTE";
    
    @Override
	public String isRole() {
		// TODO Auto-generated method stub
		return ROLE;
	}

    @Override
    public InfoUser hasInfoUser() {
        return this.infoUser;
    }
}

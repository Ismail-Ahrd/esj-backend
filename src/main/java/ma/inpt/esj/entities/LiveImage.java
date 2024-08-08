package ma.inpt.esj.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LiveImage {
	@Id
	@SequenceGenerator(name="generated",sequenceName ="generated",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "generated")
	private int id;
	
	@Lob
	byte[] imageData;
}

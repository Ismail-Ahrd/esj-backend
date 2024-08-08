package ma.inpt.esj.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.inpt.esj.entities.InfoUser;
import ma.inpt.esj.entities.Live;
import ma.inpt.esj.entities.Responsable;

@JsonTypeName("Responsable")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponsableDTO {
	private Long id;
    private InfoUser infoUser;
    private String role;
    
    public ResponsableDTO(Responsable responsable) {
		super();
		this.id = responsable.getId();
		this.infoUser = responsable.getInfoUser();
		this.role = responsable.isRole();
	}
}


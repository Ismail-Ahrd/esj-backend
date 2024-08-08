package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.inpt.esj.entities.InfoUser;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdministrateurDTO {
    private Long id;
    private InfoUser infoUser;
	
}
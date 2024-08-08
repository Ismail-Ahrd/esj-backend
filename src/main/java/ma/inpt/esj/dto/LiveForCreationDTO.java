package ma.inpt.esj.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LiveForCreationDTO {
	private int id;

    private String subject;
    private LocalDateTime date;
    private String lienStreamYard;
    private String lienYoutube;
    private ThemeDTO thematique;
    private ResponsableDTO responsable;
    
}

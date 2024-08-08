package ma.inpt.esj.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LiveDTO {
    private int id;

    private String subject;
    private LocalDateTime date;
    private String lienStreamYard;
    private String lienYoutube;
    private int img;
    private AdministrateurDTO admin;
    private List<QuestionDTO> questions;
    private ThemeDTO thematique;
    private boolean active=false;

    @JsonProperty("responsable")
    private ResponsableDTO responsable;

   public LiveDTO(int id, String subject, LocalDateTime date, String lienStreamYard, String lienYoutube) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.lienStreamYard = lienStreamYard;
        this.lienYoutube = lienYoutube;
    }
}

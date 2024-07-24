package ma.inpt.esj.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
    @AllArgsConstructor
    @NoArgsConstructor
    public  class ExperienceDTO {
        private String year;
        private String position;
        private String hospital;
    }

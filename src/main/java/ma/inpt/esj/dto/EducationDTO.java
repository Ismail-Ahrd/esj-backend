package ma.inpt.esj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
    @AllArgsConstructor
    @NoArgsConstructor
    public  class EducationDTO {
        private String year;
        private String diploma;
        private String institut;
    }
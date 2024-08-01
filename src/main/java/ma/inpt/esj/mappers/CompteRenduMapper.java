package ma.inpt.esj.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.inpt.esj.dto.CompteRenduDto;
import ma.inpt.esj.entities.CompteRendu;
import ma.inpt.esj.repositories.DiscussionRepository;

@Service
@RequiredArgsConstructor
public class CompteRenduMapper {
    private final DiscussionRepository discussionRepository;

    public CompteRendu fromCompteRendudTO(CompteRenduDto CompteRenduDto) {
        CompteRendu compteRendu = new CompteRendu();
        BeanUtils.copyProperties(CompteRenduDto, compteRendu);
        compteRendu.setDiscussion(discussionRepository.findById(CompteRenduDto.getDiscussionId()).orElse(null));
        return compteRendu;
    }

    public CompteRenduDto fromCompteRendu(CompteRendu compteRendu) {
        if(compteRendu == null) return null;
        CompteRenduDto compteRenduDto = new CompteRenduDto();
        BeanUtils.copyProperties(compteRendu, compteRenduDto);
        compteRenduDto.setDiscussionId(compteRendu.getDiscussion().getId());
        return compteRenduDto;
    }

}

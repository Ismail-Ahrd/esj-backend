package ma.inpt.esj.services;

import ma.inpt.esj.dto.CompteRenduDto;
import ma.inpt.esj.entities.CompteRendu;
import ma.inpt.esj.exception.CompteRenduException;
import ma.inpt.esj.exception.CompteRenduNotFoundException;
import ma.inpt.esj.exception.DiscussionNotFoundException;


public interface CompteRenduService {
    CompteRenduDto getCompteRenduById(Long id, Long userId) throws CompteRenduNotFoundException, DiscussionNotFoundException, CompteRenduException;
    CompteRenduDto createCompteRendu(Long userId, CompteRenduDto c) throws CompteRenduException, DiscussionNotFoundException;
    CompteRendu updateCompteRendu(Long id, CompteRendu c) throws CompteRenduNotFoundException, CompteRenduException;
}

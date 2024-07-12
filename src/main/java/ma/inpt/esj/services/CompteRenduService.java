package ma.inpt.esj.services;

import ma.inpt.esj.entities.CompteRendu;
import ma.inpt.esj.exception.CompteRenduException;
import ma.inpt.esj.exception.CompteRenduNotFoundException;


public interface CompteRenduService {
    Iterable<CompteRendu> getAllComptesRendus() throws  CompteRenduException;
    CompteRendu getCompteRenduById(Long id) throws CompteRenduNotFoundException;
    CompteRendu createCompteRendu(CompteRendu c) throws CompteRenduException;
    CompteRendu updateCompteRendu(Long id, CompteRendu c) throws CompteRenduNotFoundException, CompteRenduException;
}

package ma.inpt.esj.services;
import ma.inpt.esj.entities.Consultation;
import ma.inpt.esj.entities.DossierMedical;

import java.util.List;

public interface DossierMedicalService {

    DossierMedical getById(Long id);

    List<DossierMedical> getAllDossiersMedicaux();

    DossierMedical saveOrUpdate(DossierMedical dossierMedical);

    void deleteById(Long id);

    DossierMedical addConsultation(Long dossierMedicalId, Consultation consultation);
}

package ma.inpt.esj.services;


import ma.inpt.esj.entities.Consultation;
import ma.inpt.esj.entities.DossierMedical;
import ma.inpt.esj.repositories.ConsultationRepository;
import ma.inpt.esj.repositories.DossierMedicalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DossierMedicalServiceImpl implements DossierMedicalService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;

    @Override
    public DossierMedical getById(Long id) {
        Optional<DossierMedical> optionalDossierMedical = dossierMedicalRepository.findById(id);
        return optionalDossierMedical.orElse(null);
    }

    @Override
    public List<DossierMedical> getAllDossiersMedicaux() {
        return dossierMedicalRepository.findAll();
    }

    @Override
    public DossierMedical saveOrUpdate(DossierMedical dossierMedical) {
        return dossierMedicalRepository.save(dossierMedical);
    }

    @Override
    public void deleteById(Long id) {
        dossierMedicalRepository.deleteById(id);
    }

    @Override
    public DossierMedical addConsultation(Long dossierMedicalId, Consultation consultation) {
        DossierMedical dossierMedical = dossierMedicalRepository.findById(dossierMedicalId).orElse(null);
        if (dossierMedical != null) {
            consultation.setDossierMedical(dossierMedical);
            consultationRepository.save(consultation);
            dossierMedical.getHistoriqueConsultations().add(consultation);
            dossierMedicalRepository.save(dossierMedical);
        }
        return dossierMedical;
    }
}


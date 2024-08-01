package ma.inpt.esj.services;


import ma.inpt.esj.entities.Consultation;

import java.util.List;

public interface ConsultationService {
    Consultation saveConsultation(Consultation consultation);
    //    Consultation updateConsultation(Long id, Consultation consultation);
    void deleteConsultation(Long id);
    Consultation getConsultationById(Long id);
    List<Consultation> getAllConsultations();
}


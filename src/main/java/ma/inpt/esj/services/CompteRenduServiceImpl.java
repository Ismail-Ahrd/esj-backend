package ma.inpt.esj.services;

import ma.inpt.esj.entities.CompteRendu;
import ma.inpt.esj.exception.CompteRenduException;
import ma.inpt.esj.exception.CompteRenduNotFoundException;
import ma.inpt.esj.repositories.CompteRenduRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompteRenduServiceImpl implements CompteRenduService {

    private final CompteRenduRepository compteRenduRepository;

    @Autowired
    public CompteRenduServiceImpl(CompteRenduRepository compteRenduRepository) {
        this.compteRenduRepository = compteRenduRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<CompteRendu> getAllComptesRendus() throws CompteRenduException {
        try {
            return compteRenduRepository.findAll();
        } catch (Exception e) {
            throw new CompteRenduException("Erreur lors de la récupération des Comptes Rendus", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CompteRendu getCompteRenduById(Long id) throws CompteRenduNotFoundException {
        return compteRenduRepository.findById(id)
                .orElseThrow(() -> new CompteRenduNotFoundException("Compte Rendu avec l'identifiant " + id + " non trouvé."));
    }

    @Override
    @Transactional
    public CompteRendu createCompteRendu(CompteRendu c) throws CompteRenduException {
        if (c == null) {
            throw new IllegalArgumentException("Le compte rendu ne doit pas être nul.");
        }
        try {
            return compteRenduRepository.save(c);
        } catch (Exception e) {
            throw new CompteRenduException("Erreur lors de l'enregistrement du Compte Rendu", e);
        }
    }

    @Override
    @Transactional
    public CompteRendu updateCompteRendu(Long id, CompteRendu c) throws CompteRenduNotFoundException, CompteRenduException {
        CompteRendu existingCompteRendu = compteRenduRepository.findById(id)
                .orElseThrow(() -> new CompteRenduNotFoundException("Compte Rendu avec l'identifiant " + id + " non trouvé."));

        existingCompteRendu.setContenu(c.getContenu());
        existingCompteRendu.setDate(c.getDate());
        existingCompteRendu.setDiscussion(c.getDiscussion());

        try {
            return compteRenduRepository.save(existingCompteRendu);
        } catch (Exception e) {
            throw new CompteRenduException("Erreur lors de la mise à jour du Compte Rendu", e);
        }
    }
}

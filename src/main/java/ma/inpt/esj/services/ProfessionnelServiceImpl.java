package ma.inpt.esj.services;

import lombok.AllArgsConstructor;
import ma.inpt.esj.dto.ProfessionnelSanteDTO;
import ma.inpt.esj.entities.ConfirmationToken;
import ma.inpt.esj.entities.ProfessionnelSante;
import ma.inpt.esj.exception.ProfessionnelException;
import ma.inpt.esj.exception.ProfessionnelNotFoundException;
import ma.inpt.esj.mappers.ProfessionnelMapper;
import ma.inpt.esj.repositories.ConfirmationTokenRepository;
import ma.inpt.esj.repositories.InfoUserRepository;
import ma.inpt.esj.repositories.ProfessionnelRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ProfessionnelServiceImpl implements ProfessionnelService {

    private final ProfessionnelRepository professionnelRepository;
    private final InfoUserRepository userRepository;
    private final ProfessionnelMapper professionnelMapper;

    private ConfirmationTokenRepository confirmationTokenRepository;
    private PasswordEncoder passwordEncoder;
    private ConfirmeMailService confirmeMailService;

    @Override
    public ProfessionnelSanteDTO saveProfessionnel(ProfessionnelSante professionnelSante) throws ProfessionnelException {
        if (professionnelRepository.existsByCin(professionnelSante.getCin())) {
            throw new ProfessionnelException("Le numéro de CIN spécifié est déjà utilisé par un autre utilisateur");
        }
        if (professionnelRepository.existsByInpe(professionnelSante.getInpe())) {
            throw new ProfessionnelException("Le numéro INPE spécifié est déjà utilisé par un autre utilisateur");
        }

        if (userRepository.existsByMail(professionnelSante.getInfoUser().getMail())) {
            throw new ProfessionnelException("L'email spécifié est déjà utilisé par un autre utilisateur");
        }


        professionnelSante.getInfoUser().setMotDePasse(passwordEncoder.encode(professionnelSante.getInfoUser().getMotDePasse()));

        ProfessionnelSante savedProfessionnelSante = professionnelRepository.save(professionnelSante);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setProfessionnelSante(savedProfessionnelSante);
        confirmationToken.setCreatedDate(new Date());
        confirmationToken.setToken(token);
        confirmationTokenRepository.save(confirmationToken);

        new Thread(() -> confirmeMailService.sendConfirmationEmail(savedProfessionnelSante.getInfoUser().getMail(), token)).start();

        return professionnelMapper.fromProfessionnel(savedProfessionnelSante);
    }

    @Override
    public ProfessionnelSanteDTO getProfessionnelById(Long id) throws ProfessionnelNotFoundException {
        Optional<ProfessionnelSante> professionnelSanteOptional = professionnelRepository.findById(id);
        if (professionnelSanteOptional.isEmpty()) {
            throw new ProfessionnelNotFoundException("ProfessionnelSante non trouvé avec l'ID : " + id);
        }
        return professionnelMapper.fromProfessionnel(professionnelSanteOptional.get());
    }

    @Override
    public ProfessionnelSanteDTO updateProfessionnel(Long id, Map<String, Object> updates) throws ProfessionnelNotFoundException {
        ProfessionnelSante existingProfessionnelSante = professionnelRepository.findById(id)
                .orElseThrow(() -> new ProfessionnelNotFoundException("ProfessionnelSante not found with id " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "nom":
                    existingProfessionnelSante.getInfoUser().setNom((String) value);
                    break;
                case "prenom":
                    existingProfessionnelSante.getInfoUser().setPrenom((String) value);
                    break;
                case "mail":
                    existingProfessionnelSante.getInfoUser().setMail((String) value);
                    break;
                case "numTele":
                    existingProfessionnelSante.getInfoUser().setNumTel((String) value);
                    break;
                case "password":
                    existingProfessionnelSante.getInfoUser().setMotDePasse((String) value);
                    break;
                case "cin":
                    existingProfessionnelSante.setCin((String) value);
                    break;
                case "inpe":
                    existingProfessionnelSante.setInpe((String) value);
                    break;
                case "confirmed":
                    existingProfessionnelSante.getInfoUser().setConfirmed((Boolean) value);
                    break;
                case "isFirstAuth":
                    existingProfessionnelSante.getInfoUser().setFirstAuth((Boolean) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid attribute: " + key);
            }
        });

        userRepository.save(existingProfessionnelSante.getInfoUser());
        professionnelRepository.save(existingProfessionnelSante);

        return professionnelMapper.fromProfessionnel(existingProfessionnelSante);
    }

    @Override
    public void deleteProfessionnel(Long id) throws ProfessionnelNotFoundException, ProfessionnelException {
        Optional<ProfessionnelSante> professionnelSanteOptional = professionnelRepository.findById(id);
        if (professionnelSanteOptional.isPresent()) {
            try {
                professionnelRepository.delete(professionnelSanteOptional.get());
            } catch (Exception e) {
                throw new ProfessionnelException("Une erreur s'est produite lors de la suppression du médecin", e);
            }
        } else {
            throw new ProfessionnelNotFoundException("ProfessionnelSante non trouvé avec l'ID : " + id);
        }
    }

    @Override
    public List<ProfessionnelSanteDTO> getAllProfessionnels() {
        List<ProfessionnelSante> professionnels = professionnelRepository.findAll();
        return professionnels.stream()
                .map(professionnelMapper::fromProfessionnel)
                .collect(Collectors.toList());
    }
}

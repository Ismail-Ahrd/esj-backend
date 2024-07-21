package ma.inpt.esj.services;



import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import ma.inpt.esj.dto.MedecinResponseDTO;
import ma.inpt.esj.entities.ConfirmationToken;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.exception.MedecinException;
import ma.inpt.esj.exception.MedecinNotFoundException;
import ma.inpt.esj.mappers.MedecineMapper;
import ma.inpt.esj.repositories.ConfirmationTokenRepository;
import ma.inpt.esj.repositories.InfoUserRepository;
import ma.inpt.esj.repositories.MedecinRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.List;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class MedecinServiceImpl implements MedecinService {


    private static final long EXPIRATION_TIME_MS = 60 * 60 * 1000;

    private MedecinRepository medecinRepository;
    private InfoUserRepository userRepository;
    private MedecineMapper medecineMapper;
    private PasswordEncoder passwordEncoder;

    private ConfirmationTokenRepository confirmationTokenRepository;
    private JavaMailSender mailSender;

    private ConfirmeMailService confirmeMailService;

    public MedecinResponseDTO saveMedecin(Medecin medecin) throws MedecinException {
        System.out.println("Starting saveMedecin method");

        if (medecinRepository.existsByCin(medecin.getCin())) {
            System.out.println("CIN already exists: " + medecin.getCin());
            throw new MedecinException("Le numéro de CIN spécifié est déjà utilisé par un autre utilisateur");
        }
        if (medecinRepository.existsByInpe(medecin.getInpe())) {
            System.out.println("INPE already exists: " + medecin.getInpe());
            throw new MedecinException("Le numéro INPE spécifié est déjà utilisé par un autre utilisateur");
        }
        if (medecinRepository.existsByPpr(medecin.getPpr())) {
            System.out.println("PPR already exists: " + medecin.getPpr());
            throw new MedecinException("Le numéro PPR spécifié est déjà utilisé par un autre utilisateur");
        }
        if (userRepository.existsByMail(medecin.getInfoUser().getMail())) {
            System.out.println("Email already exists: " + medecin.getInfoUser().getMail());
            throw new MedecinException("L'email spécifié est déjà utilisé par un autre utilisateur");
        }

        System.out.println("Encoding password");
        medecin.getInfoUser().setMotDePasse(passwordEncoder.encode(medecin.getInfoUser().getMotDePasse()));

        System.out.println("Saving Medecin");
        Medecin savedMedecin = medecinRepository.save(medecin);

        System.out.println("Creating and saving confirmation token");
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setMedecin(savedMedecin);
        confirmationToken.setCreatedDate(new Date());
        confirmationToken.setToken(token);
        confirmationTokenRepository.save(confirmationToken);

        new Thread(() -> {
            System.out.println("Sending confirmation email");
            confirmeMailService.sendConfirmationEmail(savedMedecin.getInfoUser().getMail(), token);
        }).start();

        System.out.println("Returning response");
        return medecineMapper.fromMedcine(savedMedecin);
    }

    public MedecinResponseDTO getMedecinById(Long id) throws MedecinNotFoundException {
        Optional<Medecin> medecinOptional = medecinRepository.findById(id);
        if (medecinOptional.isEmpty()) {
            throw new MedecinNotFoundException("Médecin non trouvé avec l'ID : " + id);
        }
        MedecinResponseDTO medecinResponseDTO=medecineMapper.fromMedcine(medecinOptional.get());
        return medecinResponseDTO;
    }

    public MedecinResponseDTO updateMedecinPartial(Long id, Map<String, Object> updates) throws MedecinNotFoundException {
        Medecin existingMedecin = medecinRepository.findById(id)
                .orElseThrow(() -> new MedecinNotFoundException("Medecin not found with id " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "nom":
                    existingMedecin.getInfoUser().setNom((String) value);
                    break;
                case "prenom":
                    existingMedecin.getInfoUser().setPrenom((String) value);
                    break;
                case "mail":
                    existingMedecin.getInfoUser().setMail((String) value);
                    break;
                case "numTele":
                    existingMedecin.getInfoUser().setNumTel((String) value);
                    break;
                case "password":
                    existingMedecin.getInfoUser().setMotDePasse((String) value);
                    break;
                case "cin":
                    existingMedecin.setCin((String) value);
                    break;
                case "inpe":
                    existingMedecin.setInpe((String) value);
                    break;
                case "ppr":
                    existingMedecin.setPpr((String) value);
                    break;
                    case "about":
                    existingMedecin.setAbout((String) value);
                    break;
                    case "sexe":
                    existingMedecin.setSexe((String) value);
                    break;
                case "estMedcinESJ":
                    existingMedecin.setEstMedcinESJ((Boolean) value);
                    break;
                case "estGeneraliste":
                    existingMedecin.setEstGeneraliste((Boolean) value);
                    break;
                case "specialite":
                    existingMedecin.setSpecialite((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid attribute: " + key);
            }
        });

        userRepository.save(existingMedecin.getInfoUser());
        medecinRepository.save(existingMedecin);

        return medecineMapper.fromMedcine(existingMedecin);
    }

    @Override
    public void deleteMedecin(Long id) throws MedecinNotFoundException, MedecinException {
        Optional<Medecin> medecinOptional = medecinRepository.findById(id);
        if (medecinOptional.isPresent()) {
            try {
                medecinRepository.delete(medecinOptional.get());
            } catch (Exception e) {
                throw new MedecinException("Une erreur s'est produite lors de la suppression du médecin", e);
            }
        } else {
            throw new MedecinNotFoundException("Médecin non trouvé avec l'ID : " + id);
        }
    }


     @Override
    public List<MedecinResponseDTO> getAllMedecins() {
        List<Medecin> medecins = medecinRepository.findAll();
        return medecins.stream()
                .map(medecineMapper::fromMedcine)
                .collect(Collectors.toList());
    }

}

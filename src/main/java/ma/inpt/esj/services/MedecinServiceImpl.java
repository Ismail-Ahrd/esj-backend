package ma.inpt.esj.services;



import ma.inpt.esj.dto.MedecinResponseDTO;
import ma.inpt.esj.entities.ConfirmationToken;
import ma.inpt.esj.entities.Education;
import ma.inpt.esj.entities.Experience;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.exception.MedecinException;
import ma.inpt.esj.exception.MedecinNotFoundException;
import ma.inpt.esj.exception.UserNotFoundException;
import ma.inpt.esj.mappers.MedecineMapper;
import ma.inpt.esj.repositories.ConfirmationTokenRepository;
import ma.inpt.esj.repositories.InfoUserRepository;
import ma.inpt.esj.repositories.MedecinRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import java.util.stream.Collectors;

@Service
@Transactional
public class MedecinServiceImpl implements MedecinService {


    private MedecinRepository medecinRepository;
    private InfoUserRepository userRepository;
    private MedecineMapper medecineMapper;
    private PasswordEncoder passwordEncoder;
    private ConfirmationTokenRepository confirmationTokenRepository;
    private ConfirmeMailService confirmeMailService;

    private JwtEncoder jwtEncoder;


    private AuthenticationManager authenticationManagerMedecin;
    public MedecinServiceImpl(MedecinRepository medecinRepository,
                              InfoUserRepository userRepository,
                              MedecineMapper medecineMapper,
                              PasswordEncoder passwordEncoder,
                              ConfirmationTokenRepository confirmationTokenRepository,
                              ConfirmeMailService confirmeMailService, JwtEncoder jwtEncoder,
                              @Qualifier("authenticationManagerMedecin") AuthenticationManager authenticationManagerMedecin) {
        this.medecinRepository = medecinRepository;
        this.userRepository = userRepository;
        this.medecineMapper = medecineMapper;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.confirmeMailService = confirmeMailService;
        this.jwtEncoder = jwtEncoder;
        this.authenticationManagerMedecin = authenticationManagerMedecin;
    }

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
        /* if (medecinRepository.existsByPpr(medecin.getPpr())) {
            System.out.println("PPR already exists: " + medecin.getPpr());
            throw new MedecinException("Le numéro PPR spécifié est déjà utilisé par un autre utilisateur");
        } */
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
                    existingMedecin.getInfoUser().setMotDePasse(passwordEncoder.encode((String) value));
                    break;
                case "confirmed":
                    existingMedecin.getInfoUser().setConfirmed((Boolean) value);
                    break;
                case "image_url":
                    existingMedecin.getInfoUser().setImage_url((String) value);
                    break;
                case "isFirstAuth":
                    existingMedecin.getInfoUser().setFirstAuth((Boolean) value);
                    break;
                case "about":
                    existingMedecin.setAbout((String) value);
                    break;
                case "linkedin":
                    existingMedecin.setLinkedin((String) value);
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
                case "medicalStudies":
                    List<Map<String, Object>> educationUpdates = (List<Map<String, Object>>) value;                    
                    Set<Long> incomingEducationIds = new HashSet<>();
                    for (Map<String, Object> eduMap : educationUpdates) {
                        Object idObject = eduMap.get("id");
                        if (idObject instanceof Integer) {
                            incomingEducationIds.add(((Integer) idObject).longValue());
                        } else if (idObject instanceof Long) {
                            incomingEducationIds.add((Long) idObject);
                        }
                    }
                    existingMedecin.getEducations().removeIf(education -> 
                        !incomingEducationIds.contains(education.getId())
                    );
                    for (Map<String, Object> eduMap : educationUpdates) {
                        Object idObject = eduMap.get("id");
                        final Long eduId;
                        if (idObject instanceof Integer) {
                            eduId = ((Integer) idObject).longValue();
                        } else if (idObject instanceof Long) {
                            eduId = (Long) idObject;
                        } else {
                            eduId = null;
                        }
    
                        if (eduId == null) {
                            Education newEducation = new Education();
                            if (eduMap.containsKey("annee")) newEducation.setAnnee((String) eduMap.get("annee"));
                            if (eduMap.containsKey("diplome")) newEducation.setDiplome((String) eduMap.get("diplome"));
                            if (eduMap.containsKey("institut")) newEducation.setInstitut((String) eduMap.get("institut"));
                            existingMedecin.getEducations().add(newEducation);
                        } else {
                            final Long finalEduId = eduId;
                            Education existingEducation = existingMedecin.getEducations().stream()
                                    .filter(e -> e.getId().equals(finalEduId))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalArgumentException("Education not found with id " + finalEduId));
                            if (eduMap.containsKey("annee")) existingEducation.setAnnee((String) eduMap.get("annee"));
                            if (eduMap.containsKey("diplome")) existingEducation.setDiplome((String) eduMap.get("diplome"));
                            if (eduMap.containsKey("institut")) existingEducation.setInstitut((String) eduMap.get("institut"));
                        }
                    }
                    break;
                case "medicalExperience":
                    List<Map<String, Object>> experienceUpdates = (List<Map<String, Object>>) value;                    
                    Set<Long> incomingExperienceIds = new HashSet<>();
                    for (Map<String, Object> expMap : experienceUpdates) {
                        Object idObject = expMap.get("id");
                        if (idObject instanceof Integer) {
                            incomingExperienceIds.add(((Integer) idObject).longValue());
                        } else if (idObject instanceof Long) {
                            incomingExperienceIds.add((Long) idObject);
                        }
                    }
                    existingMedecin.getExperiences().removeIf(experience -> 
                        !incomingExperienceIds.contains(experience.getId())
                    );
                    for (Map<String, Object> expMap : experienceUpdates) {
                        Object idObject = expMap.get("id");
                        final Long expId;
                        if (idObject instanceof Integer) {
                            expId = ((Integer) idObject).longValue();
                        } else if (idObject instanceof Long) {
                            expId = (Long) idObject;
                        } else {
                            expId = null;
                        }
    
                        if (expId == null) {
                            Experience newExperience = new Experience();
                            if (expMap.containsKey("annee")) newExperience.setAnnee((String) expMap.get("annee"));
                            if (expMap.containsKey("hopital")) newExperience.setHopital((String) expMap.get("hopital"));
                            if (expMap.containsKey("poste")) newExperience.setPoste((String) expMap.get("poste"));
                            existingMedecin.getExperiences().add(newExperience);
                        } else {
                            final Long finalExpId = expId;
                            Experience existingExperience = existingMedecin.getExperiences().stream()
                                    .filter(e -> e.getId().equals(finalExpId))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalArgumentException("Experience not found with id " + finalExpId));
                            if (expMap.containsKey("annee")) existingExperience.setAnnee((String) expMap.get("annee"));
                            if (expMap.containsKey("hopital")) existingExperience.setHopital((String) expMap.get("hopital"));
                            if (expMap.containsKey("poste")) existingExperience.setPoste((String) expMap.get("poste"));
                        }
                    }
                    break;
    
                default:
                    throw new IllegalArgumentException("Invalid attribute: " + key);
            }
        });
    
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

    public Map<String, String> confirmAuthentification(Long id,String password) throws BadRequestException {
        try {
            // Rechercher le médecin par ID
            Medecin medecin = medecinRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Medecin not found with ID: " + id));

            // Mettre à jour le champ isFirstAuth
            medecin.getInfoUser().setFirstAuth(false);
            medecinRepository.save(medecin);

            // Authentifier le médecin pour générer un nouveau token
            Authentication authentication = authenticationManagerMedecin.authenticate(
                    new UsernamePasswordAuthenticationToken(medecin.getInfoUser().getMail(), password));

            Instant instant = Instant.now();
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            // Préparer les claims pour le JWT
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", medecin.getInfoUser().getMail());
            claims.put("role", scope);
            claims.put("id", medecin.getId());
            claims.put("nom", medecin.getInfoUser().getNom());
            claims.put("prenom", medecin.getInfoUser().getPrenom());
            claims.put("mail", medecin.getInfoUser().getMail());
            claims.put("confirmed", medecin.getInfoUser().isConfirmed());
            claims.put("isFirstAuth", medecin.getInfoUser().isFirstAuth());

            // Créer le JWT
            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(30, ChronoUnit.MINUTES))
                    .subject(medecin.getInfoUser().getMail())
                    .claim("claims", claims)
                    .build();

            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS512).build(),
                    jwtClaimsSet
            );

            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

            // Retourner le nouveau token
            return Map.of("access-token", jwt);
        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Invalid username or password");
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

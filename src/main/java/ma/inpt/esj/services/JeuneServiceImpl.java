package ma.inpt.esj.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import ma.inpt.esj.dto.JeuneDto;
import ma.inpt.esj.entities.*;
import ma.inpt.esj.enums.NiveauEtudes;
import ma.inpt.esj.enums.Sexe;
import ma.inpt.esj.exception.*;
import ma.inpt.esj.mappers.JeuneMapper;
import ma.inpt.esj.mappers.JeuneNonScolariseMapper;
import ma.inpt.esj.mappers.JeuneScolariseMapper;

import ma.inpt.esj.repositories.*;
import ma.inpt.esj.dto.ConsultationDTO;
import org.springframework.beans.factory.annotation.Autowired;

import ma.inpt.esj.repositories.ConfirmationTokenRepository;
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

@Service
@Transactional
public class JeuneServiceImpl implements JeuneService{
    

    private final JeuneMapper jeuneMapper;
    private final JeuneNonScolariseMapper jeuneNonScolariseMapper;
    private final JeuneScolariseMapper jeuneScolariseMapper;
    private final MedecinRepository medecinRepository;
    private final Validator validator;
    private final JeuneRepository jeuneRepo;

    private final DossierMedicalRepository dossierMedicalRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    private final ConfirmeMailService confirmeMailService;

    private final AuthenticationManager authenticationManagerJeune;

    private final JwtEncoder jwtEncoder;

    public JeuneServiceImpl(JeuneMapper jeuneMapper,
                            JeuneNonScolariseMapper jeuneNonScolariseMapper,
                            JeuneScolariseMapper jeuneScolariseMapper, Validator validator,
                            JeuneRepository jeuneRepo,
                            MedecinRepository medecinRepository,
                            ConfirmationTokenRepository confirmationTokenRepository,
                            PasswordEncoder passwordEncoder, ConfirmeMailService confirmeMailService,
                            @Qualifier("authenticationManagerJeune") AuthenticationManager authenticationManagerJeune,
                            JwtEncoder jwtEncoder,
                            DossierMedicalRepository dossierMedicalRepository) {
        this.jeuneMapper = jeuneMapper;
        this.jeuneNonScolariseMapper = jeuneNonScolariseMapper;
        this.jeuneScolariseMapper = jeuneScolariseMapper;
        this.medecinRepository = medecinRepository;
        this.validator = validator;
        this.jeuneRepo = jeuneRepo;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmeMailService = confirmeMailService;
        this.authenticationManagerJeune = authenticationManagerJeune;
        this.jwtEncoder = jwtEncoder;
        this.dossierMedicalRepository=dossierMedicalRepository;
    }

    @Override
    public JeuneDto saveJeune(Jeune jeune) throws EmailNonValideException, PhoneNonValideException {
        try {
            validateJeuneInfo(jeune);

            // Encoder le mot de passe
            jeune.getInfoUser().setMotDePasse(passwordEncoder.encode(jeune.getInfoUser().getMotDePasse()));
            // Calculer l'âge

            // Générer l'identifiant patient
            jeune.setIdentifiantPatient(generateIdentifiantPatient());

            // Sauvegarder l'entité
            Jeune savedJeune = jeuneRepo.save(jeune);

            // Vérifier si l'ID est généré
            if (savedJeune.getId() == null) {
                throw new RuntimeException("L'ID du jeune est null après la sauvegarde !");
            }

            // Afficher les détails du jeune sauvegardé
            System.out.println("Jeune sauvegardé : " + savedJeune);

            // Générer et sauvegarder le token de confirmation
            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = new ConfirmationToken();
            confirmationToken.setJeune(savedJeune);
            confirmationToken.setCreatedDate(new Date());
            confirmationToken.setToken(token);
            confirmationTokenRepository.save(confirmationToken);

            // Envoyer l'email de confirmation
            new Thread(() -> {
                try {
                    confirmeMailService.sendConfirmationEmail(savedJeune.getInfoUser().getMail(), token);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Erreur lors de l'envoi de l'email de confirmation", e);
                }
            }).start();

            return jeuneMapper.toDtoJeune(savedJeune);

        } catch (Exception e) {
            // Afficher l'exception complète
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'enregistrement du jeune", e);
        }
    }

    private void validateJeuneInfo(Jeune jeune) throws EmailNonValideException, PhoneNonValideException  {
        if (!validator.isValidEmail(jeune.getInfoUser().getMail())) {
            throw new EmailNonValideException("Invalid email format");
        }
        if (!validator.isValidPhoneNumber(jeune.getInfoUser().getNumTel())) {
            throw new PhoneNonValideException("Invalid phone number format");
        }
//        if (jeune.getAge() >= 16 && !isValidCIN(jeune.getCin())) {
//            throw new CinNonValideException("Invalid CIN format");
//        }
    }

    public int calculateAge(Date dateNaissance) {
        LocalDate birthDate = dateNaissance.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private int generateIdentifiantPatient() {
        return new Random().nextInt(900000) + 100000;
    }



    @Override
    public Object getJeuneById(Long id) throws JeuneNotFoundException {
        Jeune jeune = jeuneRepo.findById(id)
                .orElseThrow(() -> new JeuneNotFoundException("Jeune not found for this id :: " + id));

        if (jeune instanceof JeuneScolarise) {
            return jeuneScolariseMapper.toDtoJeuneS((JeuneScolarise) jeune);
        } else if (jeune instanceof JeuneNonScolarise) {
            return jeuneNonScolariseMapper.toDtoJeuneNS((JeuneNonScolarise) jeune);
        } else {
            return jeuneMapper.toDtoJeune(jeune);
        }
    }

    public JeuneDto updateJeunePartial(Long id, Map<String, Object> updates) throws JeuneNotFoundException {
        Jeune jeune = jeuneRepo.findById(id)
                .orElseThrow(() -> new JeuneNotFoundException("Jeune not found with id " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "nom":
                    jeune.getInfoUser().setNom((String) value);
                    break;
                case "prenom":
                    jeune.getInfoUser().setPrenom((String) value);
                    break;
                case "mail":
                    jeune.getInfoUser().setMail((String) value);
                    break;
                case "numTele":
                    jeune.getInfoUser().setNumTel((String) value);
                    break;
                case "password":
                    jeune.getInfoUser().setMotDePasse((String) value);
                    break;
                case "sexe":
                    jeune.setSexe(Sexe.valueOf((String) value));
                    break;
                case "age":
                    jeune.setAge((Integer) value);
                    break;
                case "identifiantPatient":
                    jeune.setIdentifiantPatient((Integer) value);
                    break;
                case "cin":
                    jeune.setCin((String) value);
                    break;
                case "scolarise":
                    jeune.setScolarise((Boolean) value);
                    break;
                case "confirmed":
                    jeune.getInfoUser().setConfirmed((Boolean) value);
                    break;
                case "isFirstAuth":
                    jeune.getInfoUser().setFirstAuth((Boolean) value);
                    break;
                // Mise à jour des propriétés spécifiques selon le type de jeune
                case "dernierNiveauEtudes":
                    if (jeune instanceof JeuneNonScolarise) {
                        ((JeuneNonScolarise) jeune).setDerniereNiveauEtudes(NiveauEtudes.valueOf((String) value));
                    }
                    break;
                case "situationActuelle":
                    if (jeune instanceof JeuneNonScolarise) {
                        ((JeuneNonScolarise) jeune).setEnActivite((Boolean) value);
                    }
                    break;
                case "niveauEtudesActuel":
                    if (jeune instanceof JeuneScolarise) {
                        ((JeuneScolarise) jeune).setNiveauEtudeActuel(NiveauEtudes.valueOf((String) value));
                    }
                    break;
                case "cne":
                    if (jeune instanceof JeuneScolarise) {
                        ((JeuneScolarise) jeune).setCne((String) value);
                    }
                    break;
                case "codeMASSAR":
                    if (jeune instanceof JeuneScolarise) {
                        ((JeuneScolarise) jeune).setCodeMassare((String) value);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid attribute: " + key);
            }
        });

        // Enregistrer les changements
        Jeune updatedJeune = jeuneRepo.save(jeune);


        return jeuneMapper.toDtoJeune(updatedJeune);

    }

    public void deleteJeune(Long id) {
        jeuneRepo.deleteById(id);
    }

    public List<Jeune> getAllJeunesOrderByAgeAsc() {
        return jeuneRepo.getAllJeunesOrderByAgeAsc();
    }

    public List<Jeune> getAllJeunesOrderByAgeDesc() {
        return jeuneRepo.getAllJeunesOrderByAgeDesc();
    }

    public List<Jeune> getAllJeunesOrderByNom() {
        return jeuneRepo.getAllJeunesOrderByNom();
    }

    public List<Jeune> getAllJeunesOrderByPrenom() {
        return jeuneRepo.getAllJeunesOrderByPrenom();
    }

    public List<Jeune> getAllJeunesBySexe(String sexe) {
        return jeuneRepo.getAllJeunesBySexe(sexe);
    }

    public List<Jeune> getAllJeunesByNom(String nom) {
        return jeuneRepo.getAllJeunesByNom(nom);
    }
////////////////////////////////////////////////////////
    public Jeune saveOrUpdate(Jeune jeune) {
        if (jeune.getDossierMedial() == null) {
            DossierMedical dossierMedical = new DossierMedical();
            dossierMedical.setJeune(jeune);
            jeune.setDossierMedial(dossierMedical);
        }
        return jeuneRepo.save(jeune);
    }

    public void deleteById(Long id) {
        jeuneRepo.deleteById(id);
    }

    public Jeune addConsultationDTOToJeune(Long id, ConsultationDTO consultationDTO) {
        try {
            System.out.println(consultationDTO);
            Optional<Jeune> optionalJeune = jeuneRepo.findById(id);
            if (optionalJeune.isPresent()) {
                Jeune jeune = optionalJeune.get();
                System.out.println("jeune is not null");

                Medecin medecin = medecinRepository.findById(consultationDTO.getMedecinId()).orElse(null);
                if (medecin == null) {
                    System.out.println("Medecin not found");
                    return null;
                }

                DossierMedical dossierMedical = jeune.getDossierMedial(); // Assuming this is correctly fetched
                if (dossierMedical == null) {
                    System.out.println("DossierMedical not found");
                    return null;
                }

                Consultation consultation = Consultation.builder()
                        .date(consultationDTO.getDate())
                        .motif(consultationDTO.getMotif())
                        .antecedentPersonnel(AntecedentPersonnel.builder()
                                .type(consultationDTO.getAntecedentPersonnel().getType())
                                .specification(consultationDTO.getAntecedentPersonnel().getSpecification())
                                .specificationAutre(consultationDTO.getAntecedentPersonnel().getSpecificationAutre())
                                .nombreAnnee(consultationDTO.getAntecedentPersonnel().getNombreAnnee())
                                .build())
                        .antecedentFamilial(AntecedentFamilial.builder()
                                .typeAntFam(consultationDTO.getAntecedentFamilial().getTypeAntFam())
                                .autre(consultationDTO.getAntecedentFamilial().getAutre())
                                .build())
                        .historiqueClinique(consultationDTO.getHistoriqueClinique())
                        .examenClinique(consultationDTO.getExamenClinique())
                        .examenMedical(ExamenMedical.builder()
                                .typeExamen(consultationDTO.getExamenMedical().getTypeExamen())
                                .specificationExamen(consultationDTO.getExamenMedical().getSpecificationExamen())
                                .autreSpecification(consultationDTO.getExamenMedical().getAutreSpecification())
                                .build())
                        .Diagnostic(consultationDTO.getDiagnostic())
                        .Ordonnance(consultationDTO.getOrdonnance())
                        .jeune(jeune)
                        .medecin(medecin)
                        .dossierMedical(dossierMedical)
                        .build();

                return addConsultationToJeune(id, consultation);
            } else {
                System.out.println("Jeune not found");
                return null; //
            }
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }




    @Override
    public Jeune addConsultationToJeune(Long jeuneId, Consultation consultation) {
        Jeune jeune = jeuneRepo.findById(jeuneId).orElse(null);
        if (jeune != null) {
            DossierMedical dossierMedical = jeune.getDossierMedial();
            if (dossierMedical != null) {
                consultation.setDossierMedical(dossierMedical);
                dossierMedical.getHistoriqueConsultations().add(consultation);
                jeuneRepo.save(jeune);
            }
        }
        jeune.getDossierMedial().getAntecedentsPersonnels().add(consultation.getAntecedentPersonnel());
        jeune.getDossierMedial().getAntecedentsFamiliaux().add(consultation.getAntecedentFamilial());
        return jeune;
    }

    public Jeune addAntecedentFamilialToJeune(Long id, AntecedentFamilial antecedentFamilial) {
        Optional<Jeune> jeuneOptional = jeuneRepo.findById(id);
        if (jeuneOptional.isPresent()) {
            Jeune jeune = jeuneOptional.get();
            DossierMedical dossierMedical = jeune.getDossierMedial();

            if (dossierMedical == null) {
                dossierMedical = new DossierMedical();
                jeune.setDossierMedial(dossierMedical);
            }
            if (dossierMedical.getAntecedentsFamiliaux() == null) {
                dossierMedical.setAntecedentsFamiliaux(new ArrayList<>());
            }

            dossierMedical.getAntecedentsFamiliaux().add(antecedentFamilial);

            dossierMedicalRepository.save(dossierMedical);
            jeuneRepo.save(jeune);

            return jeune;
        }
        return null;
    }

    public Jeune addAntecedentPersonnelToJeune(Long id, AntecedentPersonnel antecedentPersonnel) {
        Optional<Jeune> jeuneOptional = jeuneRepo.findById(id);
        if (jeuneOptional.isPresent()) {
            Jeune jeune = jeuneOptional.get();
            DossierMedical dossierMedical = jeune.getDossierMedial();
            if (dossierMedical == null) {
                dossierMedical = new DossierMedical();
                jeune.setDossierMedial(dossierMedical);
            }
            if (dossierMedical.getAntecedentsPersonnels() == null) {
                dossierMedical.setAntecedentsPersonnels(new ArrayList<>());
            }
            dossierMedical.getAntecedentsPersonnels().add(antecedentPersonnel);
            dossierMedicalRepository.save(dossierMedical); // Save DossierMedical first
            jeuneRepo.save(jeune); // Save Jeune after updating DossierMedical
            return jeune;
        }
        return null;
    }

    public Map<String, String> confirmAuthentification( Long id,String password) throws BadRequestException {
        try {
            // Rechercher l'utilisateur par ID
            Jeune jeune = jeuneRepo.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Jeune not found with ID: " + id));

            // Mettre à jour le champ isFirstAuth
            jeune.getInfoUser().setFirstAuth(false);
            jeuneRepo.save(jeune);

            // Authentifier l'utilisateur pour générer un nouveau token
            Authentication authentication = authenticationManagerJeune.authenticate(
                    new UsernamePasswordAuthenticationToken(jeune.getInfoUser().getMail(), password));

            Instant instant = Instant.now();
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            // Préparer les claims pour le JWT
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", jeune.getInfoUser().getMail());
            claims.put("role", scope);
            claims.put("id", jeune.getId());
            claims.put("nom", jeune.getInfoUser().getNom());
            claims.put("prenom", jeune.getInfoUser().getPrenom());
            claims.put("mail", jeune.getInfoUser().getMail());
            claims.put("confirmed", jeune.getInfoUser().isConfirmed());
            claims.put("isFirstAuth", jeune.getInfoUser().isFirstAuth());

            // Créer le JWT
            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(30, ChronoUnit.MINUTES))
                    .subject(jeune.getInfoUser().getMail())
                    .claim("claims", claims)
                    .build();

            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS512).build(),
                    jwtClaimsSet
            );

            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

            // Retourner le nouveau token
            return Map.of("access-token", jwt);
        } catch (BadCredentialsException | UserNotFoundException ex) {
            throw new BadRequestException("Unable to process request");
        }
    }
}

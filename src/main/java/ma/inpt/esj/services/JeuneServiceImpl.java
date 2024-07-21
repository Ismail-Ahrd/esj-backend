package ma.inpt.esj.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

import lombok.AllArgsConstructor;
import ma.inpt.esj.dto.JeuneDto;
import ma.inpt.esj.entities.*;
import ma.inpt.esj.enums.NiveauEtudes;
import ma.inpt.esj.enums.Sexe;
import ma.inpt.esj.exception.EmailNonValideException;
import ma.inpt.esj.exception.JeuneException;
import ma.inpt.esj.exception.JeuneNotFoundException;
import ma.inpt.esj.exception.PhoneNonValideException;
import ma.inpt.esj.mappers.JeuneMapper;
import ma.inpt.esj.mappers.JeuneNonScolariseMapper;
import ma.inpt.esj.mappers.JeuneScolariseMapper;
import ma.inpt.esj.repositories.AntecedentFamilialRepo;
import ma.inpt.esj.repositories.AntecedentPersonnelRepo;
import ma.inpt.esj.repositories.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ma.inpt.esj.repositories.JeuneRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class JeuneServiceImpl implements JeuneService{


    private final JeuneMapper jeuneMapper;
    private final JeuneNonScolariseMapper jeuneNonScolariseMapper;
    private final JeuneScolariseMapper jeuneScolariseMapper;

    private final Validator validator;
    private final JeuneRepository jeuneRepo;

    private final AntecedentFamilialRepo antecedentFamilialRepo;
    private final AntecedentPersonnelRepo antecedentPersonnelRepo;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    private ConfirmeMailService confirmeMailService;

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
    public AntecedentFamilial addAntecedentFamilial(Long jeuneId, AntecedentFamilial antecedentFamilial) {
        jeuneRepo.findById(jeuneId)
                .ifPresentOrElse(jeune -> {
                            antecedentFamilial.setJeune(jeune);
                            antecedentFamilialRepo.save(antecedentFamilial);
                        },
                        () -> {
                            throw new IllegalArgumentException("Jeune not found");
                        });
        return antecedentFamilial;
    }

    @Override
    public AntecedentPersonnel addAntecedentPersonnel(Long jeuneId, AntecedentPersonnel antecedentPersonnel) {
        jeuneRepo.findById(jeuneId)
                .ifPresentOrElse(jeune -> {
                            antecedentPersonnel.setJeune(jeune);
                            antecedentPersonnelRepo.save(antecedentPersonnel);
                        },
                        () -> {
                            throw new IllegalArgumentException("Jeune not found");
                        });
        return antecedentPersonnel;
    }


    @Override
    public Map<String, Object> getAntecedents(Long jeuneId) throws JeuneException {
        Jeune jeune = jeuneRepo.findById(jeuneId)
                .orElseThrow(() -> new JeuneException("Jeune n'existe pas"));

        Map<String, Object> result = new HashMap<>();
        result.put("AntecedentFamilial", antecedentFamilialRepo.findByJeune(jeune).orElse(null));
        result.put("AntecedentPersonnel", antecedentPersonnelRepo.findByJeune(jeune).orElse(null));

        return result;
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

    /*
    public List<Consultation> getAllConsultationByDateAsc(Long id) {
        return jeuneRepository.getAllConsultationByDateAsc(id);
    }

    public List<Consultation> getAllConsultationByDateDesc(Long id) {
        return jeuneRepository.getAllConsultationByDateDesc(id);
    }
    */

    public List<Jeune> getAllJeunesBySexe(String sexe) {
        return jeuneRepo.getAllJeunesBySexe(sexe);
    }

    public List<Jeune> getAllJeunesByNom(String nom) {
        return jeuneRepo.getAllJeunesByNom(nom);
    }

    /*
    public List<Jeune> getAllJeunesByMaladie(String maladie) {
        return jeuneRepository.getAllJeunesByMaladie(maladie);
    }
    */

    public List<Jeune> getJeunesByMedecinId(Long medecinId) {
        return jeuneRepo.findByMedecinId(medecinId);
    }
}

package ma.inpt.esj.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ma.inpt.esj.exception.*;
import ma.inpt.esj.dto.AdministrateurDTO;
import ma.inpt.esj.entities.Administrateur;
import ma.inpt.esj.entities.InfoUser;
import ma.inpt.esj.mappers.AdministrateurMapper;
import ma.inpt.esj.repositories.AdministrateurRepository;
import ma.inpt.esj.entities.ConfirmationToken;
import ma.inpt.esj.repositories.ConfirmationTokenRepository;
import ma.inpt.esj.repositories.InfoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class AdministrateurServiceImpl implements AdministrateurService{
    @Autowired
    AdministrateurRepository administrateurRepository;
    @Autowired
    private InfoUserRepository infoUserRepository;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManagerAdmin;
    private ConfirmeMailService confirmeMailService;
    private final InfoUserRepository userRepository;
    private final AdministrateurMapper administrateurMapper;
    private JwtEncoder jwtEncoder;
    public AdministrateurServiceImpl(Validator validator, AdministrateurMapper administrateurMapper,ConfirmeMailService confirmeMailService,InfoUserRepository userRepository) {
        this.administrateurMapper = administrateurMapper;
        this.userRepository = userRepository;


    }
    public List<Administrateur> getAllAdmin(){
        return this.administrateurRepository.findAll();
    }
    public Optional<Administrateur> getSingleone(Long id){
        return this.administrateurRepository.findById(id);
    }
    public void createOne(Administrateur ad){
    	InfoUser infoUser = infoUserRepository.save(ad.getInfoUser());

        ad.setInfoUser(infoUser);
        administrateurRepository.save(ad);
    }

    public void updateOne(AdministrateurDTO updatedAdministrateur) throws AdministrateurNotFoundException{
        // Vérifiez si l'administrateur existe
        if (!administrateurRepository.existsById(updatedAdministrateur.getId())) throw new AdministrateurNotFoundException("Administrateur avec ID " + updatedAdministrateur.getId() + " est introuvable.");
        if (!infoUserRepository.existsById(updatedAdministrateur.getInfoUser().getId())) throw new AdministrateurNotFoundException("InfoUser avec ID " + updatedAdministrateur.getId() + " est introuvable.");
        // Sauvegardez l'administrateur mis à jour
        infoUserRepository.save(updatedAdministrateur.getInfoUser());
    }

    public void deleteOne(Long id) throws AdministrateurNotFoundException{
        if (!administrateurRepository.existsById(id)) throw new AdministrateurNotFoundException("Administrateur avec ID " + id + " est introuvable.");
        this.administrateurRepository.deleteById(id);
    }

    public AdministrateurDTO saveAdmin(Administrateur administrateur) throws AdminException {

        if (userRepository.existsByMail(administrateur.getInfoUser().getMail())) {
            throw new AdminException("L'email spécifié est déjà utilisé par un autre utilisateur");
        }
        // Encode le mot de passe
        administrateur.getInfoUser().setMotDePasse(passwordEncoder.encode(administrateur.getInfoUser().getMotDePasse()));

        // Sauvegarder l'administrateur
        Administrateur savedAdmin = administrateurRepository.save(administrateur);

        // Générer un token de confirmation
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setAdmin(savedAdmin); // Associez l'administrateur au token
        confirmationToken.setCreatedDate(new Date()); // Date de création
        confirmationTokenRepo.save(confirmationToken);

        // Envoyer un email de confirmation de manière asynchrone
        new Thread(() -> confirmeMailService.sendConfirmationEmail(savedAdmin.getInfoUser().getMail(), token)).start();

        // Retourner le DTO de l'administrateur
        return administrateurMapper.adminToAdminDTO(savedAdmin);
    }
    public Map<String, String> confirmAuthentification(Long id, String password) throws BadRequestException {
        try {
            // Find the administrator by ID
            Administrateur administrateur = administrateurRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Administrateur not found with ID: " + id));

            // Update the isFirstAuth field
            administrateur.getInfoUser().setFirstAuth(false);
            administrateurRepository.save(administrateur);

            // Authenticate the administrator to generate a new token
            Authentication authentication = authenticationManagerAdmin.authenticate(
                    new UsernamePasswordAuthenticationToken(administrateur.getInfoUser().getMail(), password));

            Instant instant = Instant.now();
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            // Prepare claims for the JWT
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", administrateur.getInfoUser().getMail());
            claims.put("role", scope);
            claims.put("id", administrateur.getId());
            claims.put("nom", administrateur.getInfoUser().getNom());
            claims.put("prenom", administrateur.getInfoUser().getPrenom());
            claims.put("mail", administrateur.getInfoUser().getMail());
            claims.put("confirmed", administrateur.getInfoUser().isConfirmed());
            claims.put("isFirstAuth", administrateur.getInfoUser().isFirstAuth());

            // Create the JWT
            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(30, ChronoUnit.MINUTES))
                    .subject(administrateur.getInfoUser().getMail())
                    .claim("claims", claims)
                    .build();

            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS512).build(),
                    jwtClaimsSet
            );

            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

            // Return the new token
            return Map.of("access-token", jwt);
        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Invalid username or password");
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}

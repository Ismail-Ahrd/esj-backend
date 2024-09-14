package ma.inpt.esj.services;

import ma.inpt.esj.dto.ProfessionnelSanteDTO;
import ma.inpt.esj.entities.ConfirmationToken;
import ma.inpt.esj.entities.ProfessionnelSante;
import ma.inpt.esj.exception.ProfessionnelException;
import ma.inpt.esj.exception.ProfessionnelNotFoundException;
import ma.inpt.esj.exception.UserNotFoundException;
import ma.inpt.esj.mappers.ProfessionnelMapper;
import ma.inpt.esj.repositories.ConfirmationTokenRepository;
import ma.inpt.esj.repositories.InfoUserRepository;
import ma.inpt.esj.repositories.ProfessionnelRepository;
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
public class ProfessionnelServiceImpl implements ProfessionnelService {

    private final ProfessionnelRepository professionnelRepository;
    private final InfoUserRepository userRepository;
    private final ProfessionnelMapper professionnelMapper;

    private ConfirmationTokenRepository confirmationTokenRepository;
    private PasswordEncoder passwordEncoder;
    private ConfirmeMailService confirmeMailService;

    private JwtEncoder jwtEncoder;

    private final AuthenticationManager authenticationManagerProfessionelSante;

    public ProfessionnelServiceImpl(
            ProfessionnelRepository professionnelRepository,
            InfoUserRepository userRepository, ProfessionnelMapper professionnelMapper,
            ConfirmationTokenRepository confirmationTokenRepository, PasswordEncoder passwordEncoder,
            ConfirmeMailService confirmeMailService,
            JwtEncoder jwtEncoder,
            @Qualifier("authenticationManagerProfessionelSante") AuthenticationManager authenticationManagerProfessionelSante) {
        this.professionnelRepository = professionnelRepository;
        this.userRepository = userRepository;
        this.professionnelMapper = professionnelMapper;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmeMailService = confirmeMailService;
        this.jwtEncoder = jwtEncoder;
        this.authenticationManagerProfessionelSante = authenticationManagerProfessionelSante;
    }

    @Override
    public ProfessionnelSanteDTO saveProfessionnel(ProfessionnelSante professionnelSante) throws ProfessionnelException {
        if (professionnelRepository.existsByCin(professionnelSante.getCin())) {
            throw new ProfessionnelException("Le numéro de CIN spécifié est déjà utilisé par un autre utilisateur");
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

    public Map<String, String> confirmAuthentification(Long id,String password) throws BadRequestException {
        try {
            // Rechercher le médecin par ID
            ProfessionnelSante professionnelSante = professionnelRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Medecin not found with ID: " + id));

            // Mettre à jour le champ isFirstAuth
            professionnelSante.getInfoUser().setFirstAuth(false);
            professionnelRepository.save(professionnelSante);

            // Authentifier le médecin pour générer un nouveau token
            Authentication authentication = authenticationManagerProfessionelSante.authenticate(
                    new UsernamePasswordAuthenticationToken(professionnelSante.getInfoUser().getMail(), password));

            Instant instant = Instant.now();
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            // Préparer les claims pour le JWT
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", professionnelSante.getInfoUser().getMail());
            claims.put("role", scope);
            claims.put("id", professionnelSante.getId());
            claims.put("nom", professionnelSante.getInfoUser().getNom());
            claims.put("prenom", professionnelSante.getInfoUser().getPrenom());
            claims.put("mail", professionnelSante.getInfoUser().getMail());
            claims.put("confirmed", professionnelSante.getInfoUser().isConfirmed());
            claims.put("isFirstAuth", professionnelSante.getInfoUser().isFirstAuth());

            // Créer le JWT
            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(30, ChronoUnit.MINUTES))
                    .subject(professionnelSante.getInfoUser().getMail())
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

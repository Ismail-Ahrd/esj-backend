package ma.inpt.esj.securityConfig;

import ma.inpt.esj.entities.Jeune;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.entities.ProfessionnelSante;
import ma.inpt.esj.entities.Administrateur;
import ma.inpt.esj.exception.UserNotFoundException;
import ma.inpt.esj.repositories.AdministrateurRepository;
import ma.inpt.esj.repositories.JeuneRepository;
import ma.inpt.esj.repositories.MedecinRepository;
import ma.inpt.esj.repositories.ProfessionnelRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth/login")
public class SecurityController {
    private final JwtEncoder jwtEncoder;
    private final MedecinRepository medecinRepository;
    private final ProfessionnelRepository professionnelSanteRepository;
    private final JeuneRepository jeuneRepo;
    private final AdministrateurRepository administrateurRepo;
    private final AuthenticationManager authenticationManagerMedecin;
    private final AuthenticationManager authenticationManagerAdmin;
    private final AuthenticationManager authenticationManagerProfessionelSante;
    private final AuthenticationManager authenticationManagerJeune;

    public SecurityController(JwtEncoder jwtEncoder, JeuneRepository jeuneRepo, MedecinRepository medecinRepository, ProfessionnelRepository professionnelSanteRepository, AdministrateurRepository administrateurRepo,
                              @Qualifier("authenticationManagerMedecin") AuthenticationManager authenticationManagerMedecin,
                              @Qualifier("authenticationManagerProfessionelSante") AuthenticationManager authenticationManagerProfessionelSante,
                              @Qualifier("authenticationManagerJeune") AuthenticationManager authenticationManagerJeune,
                              @Qualifier("authenticationManagerAdmin") AuthenticationManager authenticationManagerAdmin, AdministrateurRepository administrateurRepo1, AuthenticationManager authenticationManagerAdmin1) {
        this.jwtEncoder = jwtEncoder;
        this.jeuneRepo=jeuneRepo;
        this.medecinRepository = medecinRepository;
        this.authenticationManagerMedecin = authenticationManagerMedecin;
        this.authenticationManagerProfessionelSante = authenticationManagerProfessionelSante;
        this.professionnelSanteRepository=professionnelSanteRepository;
        this.authenticationManagerJeune=authenticationManagerJeune;
        this.administrateurRepo = administrateurRepo;
        this.authenticationManagerAdmin = authenticationManagerAdmin;
    }

    @PostMapping("/medecins")
    public Map<String, String> loginMedcin(@RequestBody Map<String, String> loginData) throws BadRequestException,UserNotFoundException {
        String username = loginData.get("username");
        String password = loginData.get("password");

        try {
            Authentication authentication = authenticationManagerMedecin.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            Instant instant = Instant.now();

            Medecin medecin = medecinRepository.findByCinOrMail(username)
                    .orElseThrow(() -> new UserNotFoundException("Medecin not found with username: " + username));

            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            claims.put("role", scope);

            claims.put("id", medecin.getId());
            claims.put("nom", medecin.getInfoUser().getNom());
            claims.put("prenom", medecin.getInfoUser().getPrenom());
            claims.put("mail", medecin.getInfoUser().getMail());
            claims.put("confirmed", medecin.getInfoUser().isConfirmed());
            claims.put("isFirstAuth", medecin.getInfoUser().isFirstAuth());

            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(6, ChronoUnit.HOURS))
                    .subject(username)
                    .claim("claims", claims)
                    .build();

            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS512).build(),
                    jwtClaimsSet
            );

            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
            return Map.of("access-token", jwt);
        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Invalid username or password");
        }
    }

    @PostMapping("/professionelSante")
    public Map<String, String> loginProfesionnelSante(@RequestBody Map<String, String> loginData) throws BadRequestException {
        String username = loginData.get("username");
        String password = loginData.get("password");

        try {
            Authentication authentication = authenticationManagerProfessionelSante.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            Instant instant = Instant.now();

            ProfessionnelSante professionnelSante = professionnelSanteRepository.findByCinOrMail(username)
                    .orElseThrow(() -> new UserNotFoundException("professionell not found with username: " + username));

            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            claims.put("role", scope);

            claims.put("id", professionnelSante.getId());
            claims.put("nom", professionnelSante.getInfoUser().getNom());
            claims.put("prenom", professionnelSante.getInfoUser().getPrenom());
            claims.put("mail", professionnelSante.getInfoUser().getMail());
            claims.put("confirmed", professionnelSante.getInfoUser().isConfirmed());
            claims.put("isFirstAuth", professionnelSante.getInfoUser().isFirstAuth());

            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(6, ChronoUnit.HOURS))
                    .subject(username)
                    .claim("claims", claims)
                    .build();

            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS512).build(),
                    jwtClaimsSet
            );

            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
            return Map.of("access-token", jwt);
        } catch (BadCredentialsException | UserNotFoundException ex) {
            throw new BadRequestException("Invalid username or password");
        }
    }

    @PostMapping("/jeunes")
    public Map<String, String> loginJeune(@RequestBody Map<String, String> loginData) throws BadRequestException {
        String username = loginData.get("username");
        String password = loginData.get("password");

        try {
            Authentication authentication = authenticationManagerJeune.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            Instant instant = Instant.now();

            Jeune jeune = jeuneRepo.findJeuneByMailOrCinOrCNEOrCodeMASSAR(username)
                    .orElseThrow(() -> new UserNotFoundException("jeune not found with username: " + username));

            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            claims.put("role", scope);

            claims.put("id", jeune.getId());
            claims.put("nom", jeune.getInfoUser().getNom());
            claims.put("prenom", jeune.getInfoUser().getPrenom());
            claims.put("mail", jeune.getInfoUser().getMail());
            claims.put("confirmed", jeune.getInfoUser().isConfirmed());
            claims.put("isFirstAuth", jeune.getInfoUser().isFirstAuth());

            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(6, ChronoUnit.HOURS))
                    .subject(username)
                    .claim("claims", claims)
                    .build();

            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS512).build(),
                    jwtClaimsSet
            );

            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
            return Map.of("access-token", jwt);
        } catch (BadCredentialsException | UserNotFoundException ex) {
            throw new BadRequestException("Invalid username or password");
        }
    }

    @PostMapping("/administrateurs")
    public Map<String, String> loginAdministrateur(@RequestBody Map<String, String> loginData) throws BadRequestException, UserNotFoundException {
        String username = loginData.get("username");
        String password = loginData.get("password");

        try {
            // Authenticate the admin
            Authentication authentication = authenticationManagerAdmin.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // Get the current time
            Instant instant = Instant.now();

            // Retrieve the admin from the repository
            Administrateur admin = administrateurRepo.findByInfoUserMail(username)
                    .orElseThrow(() -> new UserNotFoundException("Admin not found with username: " + username));

            // Get the roles (scope) of the admin
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            // Create claims for the JWT token
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            claims.put("role", scope);
            claims.put("id", admin.getId());
            claims.put("nom", admin.getInfoUser().getNom());
            claims.put("prenom", admin.getInfoUser().getPrenom());
            claims.put("mail", admin.getInfoUser().getMail());
            claims.put("confirmed", admin.getInfoUser().isConfirmed());
            claims.put("isFirstAuth", admin.getInfoUser().isFirstAuth());

            // Build the JWT token
            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(6, ChronoUnit.HOURS))
                    .subject(username)
                    .claim("claims", claims)
                    .build();

            // Encode the JWT token
            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS512).build(),
                    jwtClaimsSet
            );

            // Return the token as a response
            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
            return Map.of("access-token", jwt);
        } catch (BadCredentialsException | UserNotFoundException ex) {
            throw new BadRequestException("Invalid username or password");
        }
    }
}




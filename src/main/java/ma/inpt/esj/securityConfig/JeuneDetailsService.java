package ma.inpt.esj.securityConfig;


import lombok.AllArgsConstructor;
import ma.inpt.esj.entities.Jeune;
import ma.inpt.esj.repositories.JeuneRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JeuneDetailsService implements UserDetailsService {
    private final JeuneRepository jeuneRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Jeune jeune = jeuneRepo.findJeuneByMailOrCinOrCNEOrCodeMASSAR(username)
                .orElseThrow(() -> new UsernameNotFoundException("Jeune not found with username: " + username));

        return User.withUsername(username)
                .password(jeune.getInfoUser().getMotDePasse())
                .roles(jeune.getROLE())
                .build();

    }
}

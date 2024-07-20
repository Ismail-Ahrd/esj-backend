package ma.inpt.esj.securityConfig;

import lombok.AllArgsConstructor;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.repositories.MedecinRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MedecinDetailsService implements UserDetailsService {
    MedecinRepository medecinRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Medecin> medecinOpt = medecinRepository.findByCinOrMail(username);

        if (medecinOpt.isPresent()) {
            Medecin medecin = medecinOpt.get();
            return User
                    .withUsername(username)
                    .password(medecin.getInfoUser().getMotDePasse())
                    .roles(medecin.getROLE()).build();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}

package ma.inpt.esj.securityConfig;

import lombok.AllArgsConstructor;
import ma.inpt.esj.entities.ProfessionnelSante;
import ma.inpt.esj.repositories.ProfessionnelRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfessionelSanteDetailsService implements UserDetailsService {
    ProfessionnelRepository professionnelSanteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ProfessionnelSante> professionnelSanteOpt = professionnelSanteRepository.findByCinOrMail(username);
        if(professionnelSanteOpt.isPresent()){
            ProfessionnelSante professionnelSante=professionnelSanteOpt.get();
            return User
                    .withUsername(username)
                    .password(professionnelSante.getInfoUser().getMotDePasse())
                    .roles(professionnelSante.getROLE()).build();
        }else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}

package ma.inpt.esj.securityConfig;

import lombok.AllArgsConstructor;
import ma.inpt.esj.entities.Administrateur;
import ma.inpt.esj.repositories.AdministrateurRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminDetailsService implements UserDetailsService {
    private final AdministrateurRepository administrateurRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Administrateur admin = administrateurRepo. findByInfoUserMail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Administrateur not found with username: " + username));

        return User.withUsername(username)
                .password(admin.getInfoUser().getMotDePasse()) // Assuming 'MotDePasse' is the password field for Administrate// Assuming `getROLE` returns the role of the admin
                .build();
    }
}


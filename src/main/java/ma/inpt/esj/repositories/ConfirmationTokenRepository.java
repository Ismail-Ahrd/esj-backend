package ma.inpt.esj.repositories;



import ma.inpt.esj.entities.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
    ConfirmationToken findByToken(String token);
}

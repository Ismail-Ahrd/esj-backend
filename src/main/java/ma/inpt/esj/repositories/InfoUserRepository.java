package ma.inpt.esj.repositories;

import ma.inpt.esj.entities.InfoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InfoUserRepository extends JpaRepository<InfoUser,Long> {
    Optional<InfoUser> findByMail(String mail);
    boolean existsByMail(String mail);

    boolean existsByNumTel(String numTel);
}

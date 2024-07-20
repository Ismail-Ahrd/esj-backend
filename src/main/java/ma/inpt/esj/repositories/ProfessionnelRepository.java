

package ma.inpt.esj.repositories;

import ma.inpt.esj.entities.ProfessionnelSante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfessionnelRepository extends JpaRepository<ProfessionnelSante, Long> {
    boolean existsByCin(String cin);
    boolean existsByInpe(String inpe);

    @Query("SELECT p FROM ProfessionnelSante p WHERE p.cin = :cin")
    Optional<ProfessionnelSante> findByCin(@Param("cin") String cin);

    @Query("SELECT p FROM ProfessionnelSante p WHERE p.infoUser.mail = :mail")
    Optional<ProfessionnelSante> findByMail(@Param("mail") String mail);

    @Query("SELECT p FROM ProfessionnelSante p WHERE p.cin = :recherche OR p.infoUser.mail = :recherche")
    Optional<ProfessionnelSante> findByCinOrMail(@Param("recherche") String recherche);
}

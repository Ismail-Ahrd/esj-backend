package ma.inpt.esj.repositories;


import ma.inpt.esj.entities.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MedecinRepository extends JpaRepository<Medecin,Long> {
    boolean existsByCin(String cin);
    boolean existsByInpe(String inpe);
    boolean existsByPpr(String ppr);


    @Query("SELECT m FROM Medecin m WHERE m.cin = :cin")
    Optional<Medecin> findByCin(@Param("cin") String cin);

    @Query("SELECT m FROM Medecin m WHERE m.infoUser.mail = :mail")
    Optional<Medecin> findByMail(@Param("mail") String mail);

    @Query("SELECT m FROM Medecin m WHERE m.cin = :recherche OR m.infoUser.mail = :recherche")
    Optional<Medecin> findByCinOrMail(@Param("recherche") String recherche);
}

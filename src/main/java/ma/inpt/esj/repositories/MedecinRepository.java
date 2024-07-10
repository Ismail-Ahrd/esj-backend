package ma.inpt.esj.repositories;


import ma.inpt.esj.entities.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedecinRepository extends JpaRepository<Medecin,Long> {
    boolean existsByCin(String cin);
    boolean existsByInpe(String inpe);
    boolean existsByPpr(String ppr);
}

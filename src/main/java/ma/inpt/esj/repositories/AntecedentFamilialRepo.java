package ma.inpt.esj.repositories;




import ma.inpt.esj.entities.AntecedentFamilial;
import ma.inpt.esj.entities.Jeune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AntecedentFamilialRepo extends JpaRepository<AntecedentFamilial,Long> {
    Optional<AntecedentFamilial> findByJeune(Jeune jeune);
}

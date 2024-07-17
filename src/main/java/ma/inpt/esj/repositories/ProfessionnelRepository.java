

package ma.inpt.esj.repositories;

import ma.inpt.esj.entities.ProfessionnelSante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionnelRepository extends JpaRepository<ProfessionnelSante, Long> {
    boolean existsByCin(String cin);
    boolean existsByInpe(String inpe);
}

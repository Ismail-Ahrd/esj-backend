package ma.inpt.esj.repositories;



import ma.inpt.esj.entities.ConfirmationToken;
import ma.inpt.esj.entities.Jeune;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.entities.ProfessionnelSante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
    ConfirmationToken findByToken(String token);

    ConfirmationToken findByMedecin(Medecin medecin);
    ConfirmationToken findByProfessionnelSante(ProfessionnelSante professionnelSante);

    ConfirmationToken findByJeune(Jeune jeune);
}

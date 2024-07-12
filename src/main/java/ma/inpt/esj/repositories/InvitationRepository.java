package ma.inpt.esj.repositories;

import ma.inpt.esj.entities.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface InvitationRepository extends JpaRepository<Invitation, Long> {
}

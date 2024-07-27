package ma.inpt.esj.repositories;

import ma.inpt.esj.entities.Invitation;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    @Query("SELECT i FROM Invitation i WHERE :medecinId MEMBER OF i.discussion.medecinsInvites AND i.status = :status")
    List<Invitation> findByMedecinIdAndStatusInDiscussion(@Param("medecinId") Long medecinId, @Param("status") InvitationStatus status);
    List<Invitation> findByMedecinInvite(Medecin medecin);
    List<Invitation> findByMedecinInviteAndStatus(Medecin medecin, InvitationStatus status);
}

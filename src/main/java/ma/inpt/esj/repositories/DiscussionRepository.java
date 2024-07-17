package ma.inpt.esj.repositories;

import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.enums.DiscussionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    @Query("SELECT d FROM Discussion d JOIN d.specialitesDemandees s JOIN Medecin m ON s = m.specialite WHERE m.id = :medecinId AND d.status = :status")
    List<Discussion> findDiscussionsByMedecinSpecialite(@Param("medecinId") Long medecinId, @Param("status") DiscussionStatus status);



    List<Discussion> findDiscussionsByStatusAndMedcinResponsable_Id( DiscussionStatus status, Long medecinId );

    @Query("SELECT d FROM Discussion d JOIN d.participants p WHERE p.id = :medecinId AND d.status IN (:statuses)")
    List<Discussion> findByParticipantIdAndStatus(@Param("medecinId") Long medecinId, @Param("statuses") List<DiscussionStatus> statuses);



}


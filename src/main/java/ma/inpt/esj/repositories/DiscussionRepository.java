package ma.inpt.esj.repositories;

import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.enums.DiscussionStatus;
import ma.inpt.esj.enums.GenreDiscussion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    @Query("SELECT d FROM Discussion d JOIN d.specialitesDemandees s JOIN Medecin m ON s = m.specialite WHERE m.id = :medecinId AND d.status = :status")
    List<Discussion> findDiscussionsByMedecinSpecialite(@Param("medecinId") Long medecinId, @Param("status") DiscussionStatus status);



    List<Discussion> findDiscussionsByStatusAndMedcinResponsable_Id( DiscussionStatus status, Long medecinId );

    @Query("SELECT d FROM Discussion d JOIN d.participants p WHERE p.id = :medecinId AND d.status IN (:statuses)")
    List<Discussion> findByParticipantIdAndStatus(@Param("medecinId") Long medecinId, @Param("statuses") List<DiscussionStatus> statuses);

    List<Discussion> findByMedcinResponsable(Medecin medcinResponsable);

    Page<Discussion> findByMedcinResponsable(Medecin medcinResponsable, Pageable pageable);
    Page<Discussion> findByMedcinResponsableAndTitreContains(Medecin medcinResponsable, String kw, Pageable pageable);
    Page<Discussion> findByMedcinResponsableAndTitreContainsAndStatus(Medecin medcinResponsable, String kw, DiscussionStatus status, Pageable pageable);
    @Query("SELECT d FROM Discussion d JOIN d.participants p WHERE p.id = :medecinId AND (:kw IS NULL OR d.titre LIKE %:kw%)")
    Page<Discussion> findDiscussionsByParticipantIdAndTitreContains(@Param("medecinId") Long medecinId, @Param("kw") String kw, Pageable pageable);
    @Query("SELECT d FROM Discussion d JOIN d.participants p WHERE p.id = :medecinId AND (:status IS NULL OR d.status = :status) AND (:kw IS NULL OR d.titre LIKE %:kw%)")
    Page<Discussion> findDiscussionsByParticipantIdAndStatusAndTitreContains(@Param("medecinId") Long medecinId, @Param("status") DiscussionStatus status, @Param("kw") String kw, Pageable pageable);
    
    @Query("SELECT d FROM Discussion d WHERE d.genre = :genre AND d.status IN (:statuses)")
    List<Discussion> findByGenreAndStatusIn(
        @Param("genre") GenreDiscussion genre,
        @Param("statuses") List<DiscussionStatus> statuses
    );
}


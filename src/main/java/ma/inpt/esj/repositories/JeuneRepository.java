package ma.inpt.esj.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import ma.inpt.esj.entities.Jeune;

public interface JeuneRepository extends JpaRepository<Jeune, Long> {

    @Query(value = "SELECT\r\n" + //
                    "j.id,\r\n" + //
                    "u.nom,\r\n" + //
                    "u.prenom,\r\n" + //
                    "j.sexe,\r\n" + //
                    "j.age,\r\n" + //
                    "c.motif,\r\n" + //
                    "c.date,\r\n" + //
                    "j.identifiant_patient,\r\n" + //
                    "j.favorite\r\n" + //
                    "FROM\r\n" + //
                    "    consultation c\r\n" + //
                    "JOIN\r\n" + //
                    "    jeune j ON c.jeune_id = j.id\r\n" + //
                    "JOIN\r\n" + //
                    "    info_user u ON j.info_user_id = u.id\r\n" + //
                    "WHERE\r\n" + //
                    "    c.medecin_responsable_id = :medecinId\r\n" + //
                    "ORDER BY c.date DESC\r\n" + //
                    "LIMIT 1", nativeQuery = true)
    List<Object[]> getMedecinPatients(@Param("medecinId") Long medecinId);

    @Query(value = "SELECT\r\n" + //
            "\tj.id,\r\n" + //
            "\ti.nom,\r\n" + //
            "\ti.prenom,\r\n" + //
            "\ti.mail,\r\n" + //
            "\ti.tel,\r\n" + //
            "\tj.cin,\r\n" + //
            "\ti.image_url,\r\n" + //
            "\tj.sexe,\r\n" + //
            "\tj.age,\r\n" + //
            "\tj.adresse,\r\n" + //
            "\tj.date_naissance,\r\n" + //
            "\tj.identifiant_patient,\r\n" + //
            "\tj.scolarise,\r\n" + //
            "\tj.niveau_etude_actuel,\r\n" + //
            "\tj.cne,\r\n" + //
            "\tj.favorite,\r\n" + //
            "\tstring_agg(DISTINCT apm.medicaux, '#') AS medicaux,\r\n" + //
            "\tstring_agg(DISTINCT apc.chirurgicaux, '#') AS chirurgicaux,\r\n" + //
            "\tstring_agg(DISTINCT aph.habitues, '#') AS habitues,\r\n" + //
            "\tstring_agg(DISTINCT afm.maladies_familiales, '#') AS maladies_familiales,\r\n" + //
            "\tstring_agg(DISTINCT o.observation, '#') AS observation,\r\n" + //
            "\tstring_agg(DISTINCT CONCAT(c.date_consultation, ';', c.motif_consultation, ';', c.diagnostic, ';', c.traitement, ';', c.recommandation), '#') AS consultation\r\n" + //
            "FROM\r\n" + //
            "\tjeune j\r\n" + //
            "JOIN\r\n" + //
            "\tinfo_user i ON j.info_user_id = i.id\r\n" + //
            "LEFT JOIN\r\n" + //
            "\tap_medicaux apm ON apm.jeune_id = j.id\r\n" + //
            "LEFT JOIN\r\n" + //
            "ap_chirurgicaux apc ON apc.jeune_id = j.id\r\n" + //
            "LEFT JOIN\r\n" + //
            "\tap_habitues aph ON aph.jeune_id = j.id\r\n" + //
            "LEFT JOIN\r\n" + //
            "af_maladies afm ON afm.jeune_id = j.id\r\n" + //
            "LEFT JOIN\r\n" + //
            "observation o ON o.jeune_id = j.id\r\n" + //
            "LEFT JOIN\r\n" + //
            "\tconsultation_op c ON c.jeune_id = j.id\r\n" + //
            "WHERE\r\n" + //
            "\tj.id = :id\r\n" + //
            "GROUP BY\r\n" + //
            "\tj.id, i.nom, i.prenom, i.mail, i.tel, j.cin, i.image_url, j.sexe, j.age, j.adresse, j.date_naissance, j.identifiant_patient, j.scolarise, j.niveau_etude_actuel, j.cne, j.favorite;", nativeQuery = true)
    Object getJeuneDossierMedical(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE jeune SET favorite = :favorite WHERE id = :id", nativeQuery = true)
    Object updateFavoriteState(@Param("id") Long id, @Param("favorite") Boolean favorite);
    
    @Query(value = "SELECT\r\n" + //
        "    j.id,\r\n" + //
        "    u.nom,\r\n" + //
        "    u.prenom,\r\n" + //
        "    j.sexe,\r\n" + //
        "    j.age\r\n" + //
        "FROM\r\n" + //
        "    consultation c\r\n" + //
        "JOIN\r\n" + //
        "    jeune j ON c.jeune_id = j.id\r\n" + //
        "JOIN\r\n" + //
        "    info_user u ON j.info_user_id = u.id\r\n" + //
        "WHERE\r\n" + //
        "    c.medecin_responsable_id = :medecinId AND j.favorite = true", nativeQuery = true)
        List<Object[]> getFavoritePatients(@Param("medecinId") Long medecinId);

    @Query("SELECT j FROM Jeune j " +
            "WHERE j.infoUser.mail = :searchParam " +
            "OR j.cin = :searchParam " +
            "OR EXISTS (SELECT s FROM JeuneScolarise s WHERE (s.cne = :searchParam OR s.codeMassare = :searchParam) AND s.id = j.id) " +
            "OR EXISTS (SELECT ns FROM JeuneNonScolarise ns WHERE (ns.cne = :searchParam OR ns.codeMassare = :searchParam) AND ns.id = j.id)")
    Optional<Jeune> findJeuneByMailOrCinOrCNEOrCodeMASSAR(@Param("searchParam") String searchParam);

    @Query("SELECT j FROM Jeune j WHERE j.infoUser.mail = :mail")
    Optional<Jeune> findByMail(@Param("mail") String mail);

    boolean existsByCin(String cin);
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM JeuneScolarise s WHERE s.cne = :cne")
    boolean existsBySCNE(@Param("cne") String cne);
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM JeuneNonScolarise s WHERE s.cne = :cne")
    boolean existsByNSCNE(@Param("cne") String cne);
    @Query("SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END FROM JeuneScolarise j WHERE j.codeMassare = :codeMassare")
    boolean existsBySCodeMassare(@Param("codeMassare") String codeMassare);

    @Query("SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END FROM JeuneNonScolarise j WHERE j.codeMassare = :codeMassare")
    boolean existsByNSCodeMassare(@Param("codeMassare") String codeMassare);

    Jeune getJeuneById(Long jeuneId);

    List<Jeune> findJeuneById(Long id);
    @Query("SELECT j FROM Jeune j WHERE j.infoUser.nom = :nom")
    List<Jeune> findJeuneByNom(@Param("nom") String nom);

    // Find by InfoUser prenom
    @Query("SELECT j FROM Jeune j WHERE j.infoUser.prenom = :prenom")
    List<Jeune> findJeuneByPrenom(@Param("prenom") String prenom);

    // Find by both InfoUser nom and prenom
    @Query("SELECT j FROM Jeune j WHERE j.infoUser.nom = :nom AND j.infoUser.prenom = :prenom")
    List<Jeune> findJeuneByNomAndPrenom(@Param("nom") String nom, @Param("prenom") String prenom);

}

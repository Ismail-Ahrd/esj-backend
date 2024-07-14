package ma.inpt.esj.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ma.inpt.esj.entities.Jeune;

public interface JeuneRepository extends JpaRepository<Jeune, Long> {

    @Query(value = "select * from jeune order by age", nativeQuery = true)
    List<Jeune> getAllJeunesOrderByAgeAsc();

    @Query(value = "select * from jeune order by age desc", nativeQuery = true)
    List<Jeune> getAllJeunesOrderByAgeDesc();

    @Query(value = "select j.* from jeune j, info_user i where j.info_user_id = i.id order by i.nom", nativeQuery = true)
    List<Jeune> getAllJeunesOrderByNom();

    @Query(value = "select j.* from jeune j, info_user i where j.info_user_id = i.id order by i.prenom", nativeQuery = true)
    List<Jeune> getAllJeunesOrderByPrenom();

    /*
    @Query(value = "select c.* from jeune j, consultation c where j.id = c.jeune_id and j.id = :id order by c.date", nativeQuery = true)
    List<Consultation> getAllConsultationByDateAsc(@Param("id") Long id);

    @Query(value = "select c.* from jeune j, consultation c where j.id = c.jeune_id and j.id = :id order by c.date desc", nativeQuery = true)
    List<Consultation> getAllConsultationByDateDesc(@Param("id") Long id);
    */

    @Query(value = "select * from jeune where sexe = :sexe", nativeQuery = true)
    List<Jeune> getAllJeunesBySexe(@Param("sexe") String sexe);

    @Query(value = "select j.* from jeune j, info_user i where j.info_user_id = i.id and i.nom = :nom", nativeQuery = true)
    List<Jeune> getAllJeunesByNom(@Param("nom") String nom);

    /*
    @Query(value = "select j.* from jeune j, dossier_medical d where d.jeune_id = j.id and d.maladies_diagnostiquees = :maladie", nativeQuery = true)
    List<Jeune> getAllJeunesByMaladie(@Param("maladie") String maladie);
    */
}

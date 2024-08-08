package ma.inpt.esj.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ma.inpt.esj.entities.Live;

public interface LiveRepository extends JpaRepository<Live,Integer> {
     public List<Live> findByAdminId(int id);
     public Optional<Live> findBySubject(String S);
     public List<Live> findByResponsableId(Long id);

     @Query("SELECT l FROM Live l WHERE l.date < :currentDateTime ORDER BY l.date DESC")
     List<Live> findLastLiveBefore(LocalDateTime currentDateTime);
}


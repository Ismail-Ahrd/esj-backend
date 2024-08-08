package ma.inpt.esj.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.inpt.esj.entities.LiveImage;

@Repository
public interface LiveImageRepository extends JpaRepository<LiveImage, Integer>{
}

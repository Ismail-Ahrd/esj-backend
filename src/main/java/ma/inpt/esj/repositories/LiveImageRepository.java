package ma.inpt.esj.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.inpt.esj.entities.LiveImage;

@Repository
public interface LiveImageRepository extends JpaRepository<LiveImage, Integer>{
}

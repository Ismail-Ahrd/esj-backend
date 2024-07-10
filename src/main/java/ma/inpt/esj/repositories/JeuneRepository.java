package ma.inpt.esj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.inpt.esj.entities.Jeune;

public interface JeuneRepository extends JpaRepository<Jeune, Long> {
    
}

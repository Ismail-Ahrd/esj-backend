package ma.inpt.esj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.inpt.esj.entities.Experience;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}

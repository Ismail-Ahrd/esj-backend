package ma.inpt.esj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.inpt.esj.entities.Education;

public interface EducationRepository extends JpaRepository<Education, Long> {
}


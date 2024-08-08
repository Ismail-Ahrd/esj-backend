package ma.inpt.esj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.inpt.esj.entities.Theme;

import java.util.Optional;

public interface ThemeRepository extends JpaRepository<Theme,Integer> {
    //public List<PropositionThématique> findByLiveId(int id);
    public Optional<Theme> findByContenu(String s);
}

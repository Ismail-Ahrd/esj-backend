package ma.inpt.esj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.inpt.esj.entities.Administrateur;

public interface AdministrateurRepository extends JpaRepository<Administrateur,Long> {
    /*public Optional<Administrateur> findByMdp(String mdp );
    public Optional<Administrateur>  findByEmail(String e);*/
}

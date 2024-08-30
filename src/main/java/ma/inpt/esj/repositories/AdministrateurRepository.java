package ma.inpt.esj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.inpt.esj.entities.Administrateur;

import java.util.Optional;

public interface AdministrateurRepository extends JpaRepository<Administrateur,Long> {
    Optional<Administrateur> findByInfoUserMail(String mail);
    /*public Optional<Administrateur> findByMdp(String mdp );
    public Optional<Administrateur>  findByEmail(String e);*/
}

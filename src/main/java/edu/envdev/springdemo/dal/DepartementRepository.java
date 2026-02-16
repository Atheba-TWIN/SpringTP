package edu.envdev.springdemo.dal;

import edu.envdev.springdemo.model.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Integer> {
  boolean existsByNomIgnoreCaseAndEntreprise_Id(String nom, Integer entrepriseId);
}

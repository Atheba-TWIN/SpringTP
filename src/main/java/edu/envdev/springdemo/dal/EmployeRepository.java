package edu.envdev.springdemo.dal;

import edu.envdev.springdemo.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Integer> {
  boolean existsByMatricule(String matricule);

  long countByDepartement_Id(Integer departementId);
}

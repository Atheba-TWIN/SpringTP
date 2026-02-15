package edu.envdev.springdemo.dal;

import edu.envdev.springdemo.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Integer> {

}
// à partir de l'entité employé, implémenter les différents services qui
// permettront de manipuler employé
package edu.envdev.springdemo.service;

import edu.envdev.springdemo.dal.EmployeRepository;
import edu.envdev.springdemo.exception.ResourceNotFoundException;
import edu.envdev.springdemo.model.Employe;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeServiceImpl implements EmployeService {
  private final EmployeRepository repository;

  public EmployeServiceImpl(EmployeRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Employe> findAll() {
    return repository.findAll();
  }

  @Override
  public Employe findById(Integer id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé avec id=" + id));
  }

  @Override
  public Employe create(Employe employe) {
    // l'ID est géré par JPA (hérité de User); sauvegarde directe
    return repository.save(employe);
  }

  @Override
  public Employe update(Integer id, Employe employe) {
    Employe exist = findById(id);
    // Mettre à jour les champs modifiables
    exist.setNom(employe.getNom());
    return repository.save(exist);
  }

  @Override
  public void deleteById(Integer id) {
    if (!repository.existsById(id)) {
      throw new ResourceNotFoundException("Employé non trouvé avec id=" + id);
    }
    repository.deleteById(id);
  }
}
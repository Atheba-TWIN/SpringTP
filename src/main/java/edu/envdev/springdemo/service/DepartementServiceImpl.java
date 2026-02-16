package edu.envdev.springdemo.service;

import edu.envdev.springdemo.dal.DepartementRepository;
import edu.envdev.springdemo.dal.EmployeRepository;
import edu.envdev.springdemo.dal.EntrepriseRepository;
import edu.envdev.springdemo.exception.ResourceNotFoundException;
import edu.envdev.springdemo.model.Departement;
import edu.envdev.springdemo.model.Entreprise;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementServiceImpl implements DepartementService {
  private static final Integer SINGLETON_ENTREPRISE_ID = 1;

  private final DepartementRepository departementRepository;
  private final EntrepriseRepository entrepriseRepository;
  private final EmployeRepository employeRepository;

  public DepartementServiceImpl(DepartementRepository departementRepository, EntrepriseRepository entrepriseRepository,
      EmployeRepository employeRepository) {
    this.departementRepository = departementRepository;
    this.entrepriseRepository = entrepriseRepository;
    this.employeRepository = employeRepository;
  }

  @Override
  public List<Departement> findAll() {
    return departementRepository.findAll();
  }

  @Override
  public Departement findById(Integer id) {
    return departementRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Departement non trouve avec id=" + id));
  }

  @Override
  public Departement create(Departement departement) {
    String nom = departement.getNom() == null ? null : departement.getNom().trim();
    if (nom == null || nom.isBlank()) {
      throw new IllegalArgumentException("Le nom du departement est obligatoire");
    }
    Entreprise entreprise = entrepriseRepository.findById(SINGLETON_ENTREPRISE_ID)
        .orElseThrow(() -> new ResourceNotFoundException("Entreprise singleton non trouvee"));
    if (departementRepository.existsByNomIgnoreCaseAndEntreprise_Id(nom, SINGLETON_ENTREPRISE_ID)) {
      throw new IllegalArgumentException("Ce departement existe deja: " + nom);
    }
    departement.setNom(nom);
    departement.setEntreprise(entreprise);
    return departementRepository.save(departement);
  }

  @Override
  public Departement update(Integer id, Departement departement) {
    Departement existing = findById(id);
    String nom = departement.getNom() == null ? null : departement.getNom().trim();
    if (nom == null || nom.isBlank()) {
      throw new IllegalArgumentException("Le nom du departement est obligatoire");
    }
    if (departementRepository.existsByNomIgnoreCaseAndEntreprise_IdAndIdNot(nom, SINGLETON_ENTREPRISE_ID, id)) {
      throw new IllegalArgumentException("Un autre departement porte deja ce nom: " + nom);
    }
    existing.setNom(nom);
    return departementRepository.save(existing);
  }

  @Override
  public void deleteById(Integer id) {
    findById(id);
    if (employeRepository.countByDepartement_Id(id) > 0) {
      throw new IllegalArgumentException("Impossible de supprimer un departement qui contient des employes");
    }
    departementRepository.deleteById(id);
  }
}

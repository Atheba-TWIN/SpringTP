package edu.envdev.springdemo.control;

import edu.envdev.springdemo.dal.DepartementRepository;
import edu.envdev.springdemo.dal.EntrepriseRepository;
import edu.envdev.springdemo.exception.ResourceNotFoundException;
import edu.envdev.springdemo.model.Departement;
import edu.envdev.springdemo.model.Entreprise;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departements")
public class DepartementController {

  private static final Integer SINGLETON_ID = 1;

  private final DepartementRepository departementRepository;
  private final EntrepriseRepository entrepriseRepository;

  public DepartementController(DepartementRepository departementRepository, EntrepriseRepository entrepriseRepository) {
    this.departementRepository = departementRepository;
    this.entrepriseRepository = entrepriseRepository;
  }

  @GetMapping
  public List<Departement> listDepartements() {
    return departementRepository.findAll();
  }

  @PostMapping
  public ResponseEntity<Departement> createDepartement(@RequestBody Map<String, String> payload) {
    String nom = payload.get("nom");
    if (nom == null || nom.isBlank()) {
      throw new IllegalArgumentException("Le nom du departement est obligatoire");
    }

    Entreprise entreprise = entrepriseRepository.findById(SINGLETON_ID)
        .orElseThrow(() -> new ResourceNotFoundException("Entreprise singleton non trouvee"));

    Departement departement = new Departement(nom, entreprise);
    Departement created = departementRepository.save(departement);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }
}

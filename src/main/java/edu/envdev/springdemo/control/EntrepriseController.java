package edu.envdev.springdemo.control;

import edu.envdev.springdemo.dal.EntrepriseRepository;
import edu.envdev.springdemo.exception.ResourceNotFoundException;
import edu.envdev.springdemo.model.Entreprise;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/entreprise")
public class EntrepriseController {

  private static final Integer SINGLETON_ID = 1;

  private final EntrepriseRepository entrepriseRepository;

  public EntrepriseController(EntrepriseRepository entrepriseRepository) {
    this.entrepriseRepository = entrepriseRepository;
  }

  @GetMapping
  public Entreprise getEntreprise() {
    return entrepriseRepository.findById(SINGLETON_ID)
        .orElseThrow(() -> new ResourceNotFoundException("Entreprise singleton non trouvee"));
  }

  @PutMapping
  public Entreprise updateEntreprise(@RequestBody Map<String, String> payload) {
    String nom = payload.get("nom");
    if (nom == null || nom.isBlank()) {
      throw new IllegalArgumentException("Le nom de l'entreprise est obligatoire");
    }

    Entreprise entreprise = entrepriseRepository.findById(SINGLETON_ID)
        .orElse(new Entreprise(SINGLETON_ID, nom));
    entreprise.setNom(nom);

    return entrepriseRepository.save(entreprise);
  }
}

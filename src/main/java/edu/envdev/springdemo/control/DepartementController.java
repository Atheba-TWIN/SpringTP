package edu.envdev.springdemo.control;

import edu.envdev.springdemo.model.Departement;
import edu.envdev.springdemo.service.DepartementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/departements")
public class DepartementController {

  private final DepartementService service;

  public DepartementController(DepartementService service) {
    this.service = service;
  }

  @GetMapping("/list")
  public String listPage(Model model, Authentication authentication) {
    model.addAttribute("departements", service.findAll());
    boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch("ROLE_ADMIN"::equals);
    model.addAttribute("isAdmin", isAdmin);
    return "departements-list";
  }

  @GetMapping("/{id}/edit")
  public String editPage(@PathVariable Integer id, Model model) {
    model.addAttribute("departement", service.findById(id));
    return "departement-edit";
  }

  @PostMapping("/{id}/edit")
  public String editSubmit(@PathVariable Integer id, @RequestParam String nom, RedirectAttributes redirectAttributes) {
    try {
      Departement payload = new Departement();
      payload.setNom(nom);
      service.update(id, payload);
      redirectAttributes.addFlashAttribute("success", "Département modifié avec succès.");
      return "redirect:/api/departements/list?updated";
    } catch (IllegalArgumentException ex) {
      redirectAttributes.addFlashAttribute("error", ex.getMessage());
      return "redirect:/api/departements/" + id + "/edit";
    }
  }

  @PostMapping("/{id}/delete")
  public String deleteFromList(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
    try {
      service.deleteById(id);
      redirectAttributes.addFlashAttribute("success", "Département supprimé avec succès.");
      return "redirect:/api/departements/list?deleted";
    } catch (IllegalArgumentException ex) {
      redirectAttributes.addFlashAttribute("error", ex.getMessage());
      return "redirect:/api/departements/list";
    }
  }

  @PostMapping("/create")
  public String createFromList(@RequestParam String nom, RedirectAttributes redirectAttributes) {
    try {
      Departement payload = new Departement();
      payload.setNom(nom);
      service.create(payload);
      redirectAttributes.addFlashAttribute("success", "Département créé avec succès.");
      return "redirect:/api/departements/list?created";
    } catch (IllegalArgumentException ex) {
      redirectAttributes.addFlashAttribute("error", ex.getMessage());
      return "redirect:/api/departements/list";
    }
  }

  @GetMapping
  @ResponseBody
  public List<Departement> all() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  @ResponseBody
  public Departement one(@PathVariable Integer id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseBody
  public ResponseEntity<Departement> create(@RequestBody Departement departement) {
    Departement created = service.create(departement);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @ResponseBody
  public Departement update(@PathVariable Integer id, @RequestBody Departement departement) {
    return service.update(id, departement);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}

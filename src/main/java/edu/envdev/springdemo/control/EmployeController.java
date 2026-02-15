package edu.envdev.springdemo.control;

import edu.envdev.springdemo.model.Employe;
import edu.envdev.springdemo.service.EmployeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/employes")
@Validated
public class EmployeController {

  private final EmployeService service;

  public EmployeController(EmployeService service) {
    this.service = service;
  }

  // Page HTML pour afficher la liste (accessible à EMPLOYEE et ADMIN)
  @GetMapping("/list")
  public String listPage(Model model) {
    List<Employe> employes = service.findAll();
    model.addAttribute("employes", employes);
    return "employes-list";
  }

  // API REST endpoints (garder compatibilité)
  @GetMapping
  @ResponseBody
  public List<Employe> all() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  @ResponseBody
  public Employe one(@PathVariable Integer id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseBody
  public ResponseEntity<Employe> create(@RequestBody Employe employe) {
    Employe created = service.create(employe);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @ResponseBody
  public Employe update(@PathVariable Integer id, @RequestBody Employe employe) {
    return service.update(id, employe);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
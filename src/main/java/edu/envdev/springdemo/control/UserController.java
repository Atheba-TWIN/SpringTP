package edu.envdev.springdemo.control;

import edu.envdev.springdemo.dal.DepartementRepository;
import edu.envdev.springdemo.model.User;
import edu.envdev.springdemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
  private final UserService userService;
  private final DepartementRepository departementRepository;

  public UserController(UserService userService, DepartementRepository departementRepository) {
    this.userService = userService;
    this.departementRepository = departementRepository;
  }

  @GetMapping("/register-form")
  public String showRegisterForm(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("departements", departementRepository.findAll());
    return "register";
  }

  @PostMapping("/register")
  public String registerUser(
      @RequestParam String password,
      @RequestParam String nom,
      @RequestParam String prenom,
      @RequestParam String matricule,
      @RequestParam String fonction,
      @RequestParam Integer departementId,
      Model model) {
    try {
      userService.createUser(password, nom, prenom, matricule, fonction, departementId);
      return "redirect:/users/register-form?success";
    } catch (Exception e) {
      model.addAttribute("error", "Erreur: " + e.getMessage());
      model.addAttribute("user", new User());
      model.addAttribute("departements", departementRepository.findAll());
      return "register";
    }
  }

  @GetMapping
  @ResponseBody
  public List<User> listUsers() {
    return userService.findAll();
  }

  @GetMapping("/{id}")
  @ResponseBody
  public User getUser(@PathVariable Integer id) {
    return userService.findById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  public String deleteUser(@PathVariable Integer id) {
    userService.deleteById(id);
    return "Utilisateur supprime avec succes";
  }
}

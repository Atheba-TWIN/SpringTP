package edu.envdev.springdemo.control;

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

  public UserController(UserService userService) {
    this.userService = userService;
  }

  // Afficher le formulaire d'inscription
  @GetMapping("/register-form")
  public String showRegisterForm(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("roles", userService.findAllRoles());
    return "register";
  }

  // Traiter la soumission du formulaire
  @PostMapping("/register")
  public String registerUser(
      @RequestParam String username,
      @RequestParam String password,
      @RequestParam String roleName,
      @RequestParam(required = false) String nom,
      Model model) {
    try {
      userService.createUser(username, password, roleName, nom);
      model.addAttribute("message", "Utilisateur créé avec succès!");
      model.addAttribute("roles", userService.findAllRoles());
      return "redirect:/users/register-form?success";
    } catch (Exception e) {
      model.addAttribute("error", "Erreur: " + e.getMessage());
      model.addAttribute("user", new User());
      model.addAttribute("roles", userService.findAllRoles());
      return "register";
    }
  }

  // API REST: lister les utilisateurs (ADMIN only)
  @GetMapping
  @ResponseBody
  public List<User> listUsers() {
    return userService.findAll();
  }

  // API REST: récupérer un utilisateur
  @GetMapping("/{id}")
  @ResponseBody
  public User getUser(@PathVariable Integer id) {
    return userService.findById(id);
  }

  // API REST: supprimer un utilisateur
  @DeleteMapping("/{id}")
  @ResponseBody
  public String deleteUser(@PathVariable Integer id) {
    userService.deleteById(id);
    return "Utilisateur supprimé avec succès";
  }
}

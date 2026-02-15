package edu.envdev.springdemo.service;

import edu.envdev.springdemo.dal.EmployeRepository;
import edu.envdev.springdemo.dal.RoleRepository;
import edu.envdev.springdemo.dal.UserRepository;
import edu.envdev.springdemo.exception.ResourceNotFoundException;
import edu.envdev.springdemo.model.Employe;
import edu.envdev.springdemo.model.Role;
import edu.envdev.springdemo.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final EmployeRepository employeRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, RoleRepository roleRepository, EmployeRepository employeRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.employeRepository = employeRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Create a user. If roleName equals "EMPLOYEE", create an Employe (subclass) so
   * the user is stored both in `users` and `employes` tables (JOINED
   * inheritance).
   * nom can be null — will default to username for employee name.
   */
  public User createUser(String username, String password, String roleName, String nom) {
    Role role = roleRepository.findByName(roleName)
        .orElseThrow(() -> new ResourceNotFoundException("Rôle non trouvé: " + roleName));

    if ("EMPLOYEE".equalsIgnoreCase(roleName)) {
      String finalNom = (nom == null || nom.isBlank()) ? username : nom;
      Employe employe = new Employe(username, passwordEncoder.encode(password), finalNom);
      employe.getRoles().add(role);
      return employeRepository.save(employe);
    }

    User user = new User(username, passwordEncoder.encode(password));
    user.getRoles().add(role);
    return userRepository.save(user);
  }

  public User findById(Integer id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec id=" + id));
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé: " + username));
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public void deleteById(Integer id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("Utilisateur non trouvé avec id=" + id);
    }
    userRepository.deleteById(id);
  }

  public Role createRole(String roleName) {
    Role role = new Role(roleName);
    return roleRepository.save(role);
  }

  public Role findRoleByName(String name) {
    return roleRepository.findByName(name)
        .orElseThrow(() -> new ResourceNotFoundException("Rôle non trouvé: " + name));
  }

  public List<Role> findAllRoles() {
    return roleRepository.findAll();
  }
}

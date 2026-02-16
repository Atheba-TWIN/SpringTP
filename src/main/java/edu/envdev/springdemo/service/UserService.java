package edu.envdev.springdemo.service;

import edu.envdev.springdemo.dal.DepartementRepository;
import edu.envdev.springdemo.dal.EmployeRepository;
import edu.envdev.springdemo.dal.RoleRepository;
import edu.envdev.springdemo.dal.UserRepository;
import edu.envdev.springdemo.exception.ResourceNotFoundException;
import edu.envdev.springdemo.model.Departement;
import edu.envdev.springdemo.model.Employe;
import edu.envdev.springdemo.model.Role;
import edu.envdev.springdemo.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class UserService {
  private static final String EMPLOYEE_ROLE = "EMPLOYEE";

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final EmployeRepository employeRepository;
  private final DepartementRepository departementRepository;
  private final EmployeService employeService;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, RoleRepository roleRepository, EmployeRepository employeRepository,
      DepartementRepository departementRepository, EmployeService employeService, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.employeRepository = employeRepository;
    this.departementRepository = departementRepository;
    this.employeService = employeService;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Public registration flow: always creates an EMPLOYEE user.
   */
  public User createUser(String password, String nom, String prenom, String matricule, String fonction,
      Integer departementId) {
    if (employeRepository.existsByMatricule(matricule)) {
      throw new IllegalArgumentException("Matricule deja utilise: " + matricule);
    }
    String username = matricule.trim().toLowerCase(Locale.ROOT);
    if (userRepository.findByUsername(username).isPresent()) {
      throw new IllegalArgumentException("Username deja utilise: " + username);
    }

    Role role = roleRepository.findByName(EMPLOYEE_ROLE)
        .orElseGet(() -> roleRepository.save(new Role(EMPLOYEE_ROLE)));

    Departement departement = departementRepository.findById(departementId)
        .orElseThrow(() -> new ResourceNotFoundException("Departement non trouve avec id=" + departementId));

    Employe employe = new Employe(username, passwordEncoder.encode(password), nom, prenom, matricule, fonction);
    employe.setDepartement(departement);
    employe.setEntreprise(departement.getEntreprise());
    employe.getRoles().add(role);

    return employeService.create(employe);
  }

  public User findById(Integer id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouve avec id=" + id));
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouve: " + username));
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public void deleteById(Integer id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("Utilisateur non trouve avec id=" + id);
    }
    userRepository.deleteById(id);
  }

  public Role createRole(String roleName) {
    Role role = new Role(roleName);
    return roleRepository.save(role);
  }

  public Role findRoleByName(String name) {
    return roleRepository.findByName(name)
        .orElseThrow(() -> new ResourceNotFoundException("Role non trouve: " + name));
  }

  public List<Role> findAllRoles() {
    return roleRepository.findAll();
  }
}

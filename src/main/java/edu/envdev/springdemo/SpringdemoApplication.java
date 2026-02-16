package edu.envdev.springdemo;

import edu.envdev.springdemo.dal.DepartementRepository;
import edu.envdev.springdemo.dal.EntrepriseRepository;
import edu.envdev.springdemo.dal.RoleRepository;
import edu.envdev.springdemo.dal.UserRepository;
import edu.envdev.springdemo.model.Departement;
import edu.envdev.springdemo.model.Entreprise;
import edu.envdev.springdemo.model.Role;
import edu.envdev.springdemo.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringdemoApplication {

  private static final int SINGLETON_ENTREPRISE_ID = 1;

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final EntrepriseRepository entrepriseRepository;
  private final DepartementRepository departementRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.bootstrap.admin.username:}")
  private String bootstrapAdminUsername;

  @Value("${app.bootstrap.admin.password:}")
  private String bootstrapAdminPassword;

  @Value("${app.bootstrap.entreprise.nom:Entreprise Demo}")
  private String bootstrapEntrepriseNom;

  @Value("${app.bootstrap.departement.nom:General}")
  private String bootstrapDepartementNom;

  public SpringdemoApplication(RoleRepository roleRepository, UserRepository userRepository,
      EntrepriseRepository entrepriseRepository, DepartementRepository departementRepository,
      PasswordEncoder passwordEncoder) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.entrepriseRepository = entrepriseRepository;
    this.departementRepository = departementRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringdemoApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void initializeData() {
    Role adminRole = roleRepository.findByName("ADMIN")
        .orElseGet(() -> roleRepository.save(new Role("ADMIN")));
    roleRepository.findByName("EMPLOYEE")
        .orElseGet(() -> roleRepository.save(new Role("EMPLOYEE")));

    Entreprise entreprise = entrepriseRepository.findById(SINGLETON_ENTREPRISE_ID)
        .orElseGet(() -> entrepriseRepository.save(new Entreprise(SINGLETON_ENTREPRISE_ID, bootstrapEntrepriseNom)));

    if (!departementRepository.existsByNomIgnoreCaseAndEntreprise_Id(bootstrapDepartementNom, SINGLETON_ENTREPRISE_ID)) {
      departementRepository.save(new Departement(bootstrapDepartementNom, entreprise));
    }
    if (!departementRepository.existsByNomIgnoreCaseAndEntreprise_Id("Ressources Humaines", SINGLETON_ENTREPRISE_ID)) {
      departementRepository.save(new Departement("Ressources Humaines", entreprise));
    }
    if (!departementRepository.existsByNomIgnoreCaseAndEntreprise_Id("Informatique", SINGLETON_ENTREPRISE_ID)) {
      departementRepository.save(new Departement("Informatique", entreprise));
    }

    boolean adminConfigured = bootstrapAdminUsername != null && !bootstrapAdminUsername.isBlank()
        && bootstrapAdminPassword != null && !bootstrapAdminPassword.isBlank();

    if (adminConfigured && userRepository.findByUsername(bootstrapAdminUsername).isEmpty()) {
      User admin = new User(bootstrapAdminUsername, passwordEncoder.encode(bootstrapAdminPassword));
      admin.getRoles().add(adminRole);
      userRepository.save(admin);
      System.out.println("Bootstrap admin user created from properties.");
    }
  }
}

package edu.envdev.springdemo;

import edu.envdev.springdemo.dal.RoleRepository;
import edu.envdev.springdemo.dal.UserRepository;
import edu.envdev.springdemo.model.Role;
import edu.envdev.springdemo.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringdemoApplication {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public SpringdemoApplication(RoleRepository roleRepository, UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringdemoApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void initializeRolesAndUsers() {
    // Créer les rôles s'ils n'existent pas
    if (roleRepository.findByName("ADMIN").isEmpty()) {
      roleRepository.save(new Role("ADMIN"));
    }
    if (roleRepository.findByName("EMPLOYEE").isEmpty()) {
      roleRepository.save(new Role("EMPLOYEE"));
    }

    // Créer les utilisateurs par défaut s'ils n'existent pas
    if (userRepository.findByUsername("admin").isEmpty()) {
      User admin = new User("admin", passwordEncoder.encode("admin123"));
      admin.getRoles().add(roleRepository.findByName("ADMIN").get());
      userRepository.save(admin);
    }

    if (userRepository.findByUsername("employe").isEmpty()) {
      User employee = new User("employe", passwordEncoder.encode("employe123"));
      employee.getRoles().add(roleRepository.findByName("EMPLOYEE").get());
      userRepository.save(employee);
    }

    System.out.println("✓ Rôles et utilisateurs initialisés avec succès");
    System.out.println("  - Admin: username='admin', password='admin123'");
    System.out.println("  - Employee: username='employe', password='employe123'");
  }
}
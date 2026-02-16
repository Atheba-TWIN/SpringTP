package edu.envdev.springdemo.config;

import edu.envdev.springdemo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  private final CustomUserDetailsService userDetailsService;

  public SecurityConfig(CustomUserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/index.html", "/css/**", "/js/**").permitAll()
            .requestMatchers("/login").permitAll()
            .requestMatchers("/403").permitAll()
            .requestMatchers("/users/register", "/users/register-form").permitAll()

            .requestMatchers("/api/employes/list").hasAnyRole("EMPLOYEE", "ADMIN")

            .requestMatchers(HttpMethod.GET, "/api/employes/**").hasAnyRole("EMPLOYEE", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/employes/*/edit").hasAnyRole("EMPLOYEE", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/employes/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/employes/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/employes/**").hasRole("ADMIN")

            .requestMatchers(HttpMethod.GET, "/api/entreprise").hasAnyRole("EMPLOYEE", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/entreprise").hasRole("ADMIN")

            .requestMatchers(HttpMethod.GET, "/api/departements/**").hasAnyRole("EMPLOYEE", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/departements/**").hasRole("ADMIN")

            .requestMatchers("/users/**").hasRole("ADMIN")
            .anyRequest().authenticated())

        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/api/employes/list", true)
            .permitAll())
        .logout(logout -> logout
            .logoutSuccessUrl("/login?logout")
            .permitAll())
        .exceptionHandling(ex -> ex.accessDeniedPage("/403"));

    return http.build();
  }
}

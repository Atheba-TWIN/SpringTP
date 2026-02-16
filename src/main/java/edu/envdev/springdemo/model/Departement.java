package edu.envdev.springdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departements")
public class Departement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String nom;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "entreprise_id", nullable = false)
  private Entreprise entreprise;

  @OneToMany(mappedBy = "departement")
  @JsonIgnore
  private List<Employe> employes = new ArrayList<>();

  public Departement() {
  }

  public Departement(String nom, Entreprise entreprise) {
    this.nom = nom;
    this.entreprise = entreprise;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public Entreprise getEntreprise() {
    return entreprise;
  }

  public void setEntreprise(Entreprise entreprise) {
    this.entreprise = entreprise;
  }

  public List<Employe> getEmployes() {
    return employes;
  }

  public void setEmployes(List<Employe> employes) {
    this.employes = employes;
  }
}

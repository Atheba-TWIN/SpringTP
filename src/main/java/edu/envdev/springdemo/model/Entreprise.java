package edu.envdev.springdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entreprises")
public class Entreprise {

  @Id
  private Integer id;

  @Column(nullable = false)
  private String nom;

  @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<Departement> departements = new ArrayList<>();

  @OneToMany(mappedBy = "entreprise")
  @JsonIgnore
  private List<Employe> employes = new ArrayList<>();

  public Entreprise() {
  }

  public Entreprise(Integer id, String nom) {
    this.id = id;
    this.nom = nom;
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

  public List<Departement> getDepartements() {
    return departements;
  }

  public void setDepartements(List<Departement> departements) {
    this.departements = departements;
  }

  public List<Employe> getEmployes() {
    return employes;
  }

  public void setEmployes(List<Employe> employes) {
    this.employes = employes;
  }
}

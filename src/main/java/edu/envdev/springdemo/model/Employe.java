package edu.envdev.springdemo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "employes")
@PrimaryKeyJoinColumn(name = "id")
public class Employe extends User {

  private String nom;

  // Constructeur vide obligatoire pour JPA
  public Employe() {
    super();
  }

  // Constructeur pratique
  public Employe(String username, String password, String nom) {
    super(username, password);
    this.nom = nom;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

}

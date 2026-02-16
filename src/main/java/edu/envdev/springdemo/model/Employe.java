package edu.envdev.springdemo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "employes")
@PrimaryKeyJoinColumn(name = "id")
public class Employe extends User {

  @Column(nullable = false)
  private String nom;

  @Column(nullable = false)
  private String prenom;

  @Column(nullable = false, unique = true)
  private String matricule;

  @Column(nullable = false)
  private String fonction;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "entreprise_id")
  private Entreprise entreprise;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "departement_id", nullable = false)
  private Departement departement;

  public Employe() {
    super();
  }

  public Employe(String username, String password, String nom, String prenom, String matricule, String fonction) {
    super(username, password);
    this.nom = nom;
    this.prenom = prenom;
    this.matricule = matricule;
    this.fonction = fonction;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getPrenom() {
    return prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public String getMatricule() {
    return matricule;
  }

  public void setMatricule(String matricule) {
    this.matricule = matricule;
  }

  public String getFonction() {
    return fonction;
  }

  public void setFonction(String fonction) {
    this.fonction = fonction;
  }

  public Entreprise getEntreprise() {
    return entreprise;
  }

  public void setEntreprise(Entreprise entreprise) {
    this.entreprise = entreprise;
  }

  public Departement getDepartement() {
    return departement;
  }

  public void setDepartement(Departement departement) {
    this.departement = departement;
  }
}

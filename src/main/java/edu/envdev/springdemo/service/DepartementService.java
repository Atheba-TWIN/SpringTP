package edu.envdev.springdemo.service;

import edu.envdev.springdemo.model.Departement;

import java.util.List;

public interface DepartementService {
  List<Departement> findAll();

  Departement findById(Integer id);

  Departement create(Departement departement);

  Departement update(Integer id, Departement departement);

  void deleteById(Integer id);
}

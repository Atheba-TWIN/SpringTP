package edu.envdev.springdemo.service;

import edu.envdev.springdemo.model.Employe;

import java.util.List;

public interface EmployeService {
  List<Employe> findAll();

  Employe findById(Integer id);

  Employe create(Employe employe);

  Employe update(Integer id, Employe employe);

  void deleteById(Integer id);
}
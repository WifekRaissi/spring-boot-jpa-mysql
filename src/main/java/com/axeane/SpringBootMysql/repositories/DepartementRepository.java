package com.axeane.SpringBootMysql.repositories;

import com.axeane.SpringBootMysql.model.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartementRepository extends JpaRepository<Departement, Long> {

    List<Departement> findDepartementByNom(String nom);

    Departement findDepartementById(Long id);
}

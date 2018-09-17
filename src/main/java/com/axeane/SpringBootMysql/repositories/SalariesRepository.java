package com.axeane.SpringBootMysql.repositories;

import com.axeane.SpringBootMysql.model.Salarie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalariesRepository extends JpaRepository<Salarie, Long> {
    List<Salarie> findSalarieByNom(String nom);

    Salarie findSalarieById(Long id);

}
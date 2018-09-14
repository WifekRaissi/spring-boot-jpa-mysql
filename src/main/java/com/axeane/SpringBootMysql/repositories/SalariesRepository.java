package com.axeane.SpringBootMysql.repositories;

import com.axeane.SpringBootMysql.model.Salarie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SalariesRepository extends JpaRepository<Salarie, Long> {
    List<Salarie> findSalarieByNom(String nom);

    Salarie findSalarieById(Long id);
    Page<Salarie> findByDepartementId(Long postId, Pageable pageable);

}

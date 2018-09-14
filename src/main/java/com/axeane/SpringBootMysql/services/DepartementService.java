package com.axeane.SpringBootMysql.services;

import com.axeane.SpringBootMysql.model.Departement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

public interface DepartementService {
    Page<Departement> getAllDepartments(Pageable pageable);

    void createDepartment(Departement departement);

    void updateDepartement(Departement departementRequest);

    void deleteDepartment(@PathVariable Long departementId);
}

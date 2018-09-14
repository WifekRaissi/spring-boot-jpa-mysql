package com.axeane.SpringBootMysql.services;

import com.axeane.SpringBootMysql.model.Salarie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SalariesService {

    void addsalarie(Long departementId, Salarie salarie);

    Page<Salarie> getAllSalariesByDepartementtId(Long departementId, Pageable pageable);

    void deleteSalaried(Long departementId, Long salarieId);

    void updateSalarie(Long departementId, Salarie salarieRequest);

    Salarie findSalarieById(Long id);
}
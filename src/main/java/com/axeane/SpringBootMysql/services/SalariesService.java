package com.axeane.SpringBootMysql.services;

import com.axeane.SpringBootMysql.model.Salarie;

import java.util.List;

public interface SalariesService {
    void addsalarie(Salarie salarie);

    List<Salarie> getListSalaries();

    Salarie findSalariedById(Long searchedId);

    void deleteSalaried(Long id);

    void updateSalarie(Salarie salaried);
}
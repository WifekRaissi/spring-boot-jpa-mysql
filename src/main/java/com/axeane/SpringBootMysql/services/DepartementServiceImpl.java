package com.axeane.SpringBootMysql.services;

import com.axeane.SpringBootMysql.model.Departement;
import com.axeane.SpringBootMysql.repositories.DepartementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DepartementServiceImpl implements DepartementService {
    private final DepartementRepository departementRepository;

    public DepartementServiceImpl(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }

    @Override
    public Page<Departement> getAllDepartments(Pageable pageable) {
        return departementRepository.findAll(pageable);
    }

    @Override
    public void createDepartment(Departement departement) {
        departementRepository.save(departement);
    }

    @Override
    public void updateDepartement(Departement departementRequest) {
        departementRepository.findById(departementRequest.getId()).map(departement -> {
            departement.setNom(departementRequest.getNom());
            departement.setDescription(departementRequest.getDescription());
            return departementRepository.save(departement);
        });
    }

    @Override
    public void deleteDepartment(Long departementId) {
        departementRepository.findById(departementId).map(departement -> {
            departementRepository.delete(departement);
            return departement;
        });
    }

}

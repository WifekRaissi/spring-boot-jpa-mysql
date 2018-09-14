package com.axeane.SpringBootMysql.services;

import com.axeane.SpringBootMysql.ResourceNotFoundException;
import com.axeane.SpringBootMysql.model.Salarie;
import com.axeane.SpringBootMysql.repositories.DepartementRepository;
import com.axeane.SpringBootMysql.repositories.SalariesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;


@Service
public class SalariesServiceImpl implements SalariesService {
    private final SalariesRepository salariesRepository;
    private final DepartementRepository departementRepository;

    public SalariesServiceImpl(SalariesRepository salariesRepository, DepartementRepository departementRepository) {
        this.salariesRepository = salariesRepository;
        this.departementRepository = departementRepository;
    }

    private Logger logger = LoggerFactory.getLogger(SalariesServiceImpl.class);

    @Override
    public void addsalarie(Long departementId, Salarie salarie) {
        departementRepository.findById(departementId).map(departement -> {
            salarie.setDepartement(departement);
            return salariesRepository.save(salarie);
        });
    }

    @Override
    public Page<Salarie> getAllSalariesByDepartementtId(Long departementId,
                                                        Pageable pageable) {
        return salariesRepository.findByDepartementId(departementId, pageable);
    }

    @Override
    public void deleteSalaried(Long departementId, Long salarieId) {
        if (!departementRepository.existsById(departementId)) {
            throw new ResourceNotFoundException("departementId" + departementId + " not found");
        }
        salariesRepository.findById(salarieId).map(salarie -> {
            salariesRepository.delete(salarie);
            return salarie;
        });
    }

    @DeleteMapping("/departements/{departementId}/salaries/{salarieId}")
    public ResponseEntity<?> deleteSalarie(@PathVariable(value = "departementId") Long departementId,
                                           @PathVariable(value = "salarieId") Long salarieId) {
        if (!departementRepository.existsById(departementId)) {
            throw new ResourceNotFoundException("departementId" + departementId + " not found");
        }
        return salariesRepository.findById(salarieId).map(salarie -> {
            salariesRepository.delete(salarie);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("CommentId " + salarieId + " not found"));
    }

    @Override
    public void updateSalarie(Long departementId, Salarie salarieRequest) {
        if (!departementRepository.existsById(departementId)) {
            throw new ResourceNotFoundException("departementId " + departementId + " not found");
        }
        salariesRepository.findById(salarieRequest.getId()).map(salarie -> {
            salarie.setNom(salarieRequest.getNom());
            salarie.setPrenom(salarieRequest.getPrenom());
            salarie.setAdresse(salarieRequest.getAdresse());
            salarie.setSalaire(salarieRequest.getSalaire());
            return salariesRepository.save(salarie);
        });
    }
    @Override
    public Salarie findSalarieById(Long id){
        return salariesRepository.findSalarieById(id);

    }

}
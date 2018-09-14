package com.axeane.SpringBootMysql.controllers;

import com.axeane.SpringBootMysql.model.Departement;
import com.axeane.SpringBootMysql.services.DepartementService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class DepartementController {


    private final DepartementService departementService;

    public DepartementController(DepartementService departementService) {
        this.departementService = departementService;
    }

    @ApiOperation(value = "the list of departments", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/departements")
    public ResponseEntity getAllDepartments(Pageable pageable) {
        Page<Departement> departements = departementService.getAllDepartments(pageable);
        return new ResponseEntity<>(departements, HttpStatus.OK);
    }

    @ApiOperation(value = "add a new department")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created department")}
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/departements")
    public ResponseEntity createDepartment(@Valid @RequestBody Departement departement) {
        departementService.createDepartment(departement);
        return new ResponseEntity<>(departement, HttpStatus.CREATED);
    }

    @ApiOperation(value = "update a department")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated department"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @PutMapping("/departements")
    public ResponseEntity updateDepartement(@Valid @RequestBody Departement departement) {
        departementService.updateDepartement(departement);
        return new ResponseEntity<>(departement, HttpStatus.OK); }

    @ApiOperation(value = "delete a department")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted department"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @DeleteMapping("/departements/{departementId}")
    public ResponseEntity deleteDepartment(@PathVariable(value = "departementId") Long departementId) {
        departementService.deleteDepartment(departementId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
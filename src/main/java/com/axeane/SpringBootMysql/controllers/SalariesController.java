package com.axeane.SpringBootMysql.controllers;

import com.axeane.SpringBootMysql.model.Salarie;
import com.axeane.SpringBootMysql.services.SalariesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.validation.ConstraintViolationProblemModule;

import javax.validation.Valid;

@RestController
@Api(value = "gestion des salariés", description = "Operations pour la gestion des salariés")
public class SalariesController {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer problemObjectMapperModules() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.modules(
                new ProblemModule(),
                new ConstraintViolationProblemModule());
    }

    private final SalariesService salariesService;

    public SalariesController(SalariesService salariesService) {
        this.salariesService = salariesService;
    }

    @ApiOperation(value = "View a list of salaries by department", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @GetMapping("/departements/{departementId}/salaries")
    public ResponseEntity getAllSalariesByDepartementId(@PathVariable(value = "departementId") Long departementId, Pageable pageable) {
        Page<Salarie> salaries = salariesService.getAllSalariesByDepartementtId(departementId, pageable);
        return new ResponseEntity<>(salaries, HttpStatus.OK);
    }

    @ApiOperation(value = "add a new salaried")
    @PostMapping("/departements/{departementId}/salaries")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created salaried")}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity createSalarie(@PathVariable(value = "departementId") Long departementId,
                                        @Valid @RequestBody Salarie salarie) {
        salariesService.addsalarie(departementId, salarie);
        return new ResponseEntity<>(salarie, HttpStatus.CREATED);
    }

    @ApiOperation(value = "update a salaried")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated salaried"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @PutMapping("/departements/{departementId}/salaries/{salarieId}")
    public ResponseEntity updateSalarie(@PathVariable(value = "departementId") Long departementId,
                                        @Valid @RequestBody Salarie salarieRequest) {
        salariesService.updateSalarie(departementId, salarieRequest);
        return new ResponseEntity<>(salarieRequest, HttpStatus.OK);
    }

    @ApiOperation(value = "delete a salaried")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted salaried"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @DeleteMapping("/departements/{departementId}/salaries/{salarieId}")
    public ResponseEntity deleteSalarie(@PathVariable(value = "departementId") Long departementId,
                                        @PathVariable(value = "salarieId") Long salarieId) {
        salariesService.deleteSalaried(departementId, salarieId);
        return new ResponseEntity(HttpStatus.OK);
    }
}

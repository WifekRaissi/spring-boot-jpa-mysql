 
 
 # Spring-Boot-Rest-Data-mysql
 
Dans le tutorial  précédent on a commencé à implementer une application Spring Boot avec le système Restful et on a intégré des validateurs pour contrôler les donées envoyées en utilisant le controllerAdvice et puis on réalisé des tests unitaires et d'intégration pour s'assurer du bon fonctionnement de l'application et pour décrire notre API on a intégré Swagger.
https://github.com/WifekRaissi/spring-boot-rest

Durant ce tutorial on continuera avec la même application pour intégrer une base de données MySQL. 

## Outils

MySQL

Avant de commencer il faut créer la base de données et la table salarie


```CREATE DATABASE spring;
  
   use spring;

    CREATE TABLE salarie (
   id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(500) NOT NULL,
    prenom VARCHAR(500) NOT NULL,
    salaire DECIMAL  NOT NULL,
    adresse VARCHAR(100) NOT NULL


);



```
L'architecture de l'application est la suivante.

   ![alt text](https://github.com/WifekRaissi/spring-boot-rest-data-mysql/blob/master/src/main/resources/images/architecture.PNG)
   

# I. Configuration de MySQL 

On commence par ajouter les dépendances nécessaires pour assurer la connexion entre Spring Boot et MySQL.
```
              <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency>
```
 ## application.properties
 Pour communiquer l'application Spring Boot avec la base de données créée précédemment on doit la configurer dans application.properties.
 
 ```
spring.datasource.url = jdbc:mysql://localhost:3306/spring?useSSL=false
spring.datasource.username = MySQL username
spring.datasource.password = MySQL password
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect
spring.jpa.hibernate.ddl-auto = update
 ```
 ## Repository
 ## SalariesRepository
 
 Il s'agit d'une interface qui étend JpaRepository, qui présente toutes les fonctionalités de CrudRepository et  PagingAndSortingRepository. https://stackoverflow.com/a/14025100 
 
C'est à travers de cette interface que l'application peut communiquer avec la base de données et faire les diffèrentes opérations.
 
 ```
 import com.axeane.SpringBootMysql.model.Salarie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SalariesRepository extends JpaRepository<Salarie, Long> {
    List<Salarie> findSalarieByNom(String nom);

    Salarie findSalarieById(Long id);

}
 ```
 


## Salarie.java

On doit maintenant ajouter quelques annotations dans la classe salarie.
```
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class Salarie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private static final AtomicInteger count = new AtomicInteger(0);

    @NotEmpty
    @NotNull
    private String nom;

    @NotEmpty
    @NotNull
    private String prenom;

    @NotNull
    private BigDecimal salaire;

    @NotEmpty
    @NotNull
    @Size(max = 256, message = "address should have maximum 256 characters")
    private String adresse;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public BigDecimal getSalaire() {
        return salaire;
    }

    public void setSalaire(BigDecimal salaire) {
        this.salaire = salaire;
    }

    public String getAdresse() {
        return adresse;
    }

    @Required
    public void setAdresse(String adresse) {

        this.adresse = adresse;
    }

    public Salarie() {
    }

    public Salarie(Long id, String nom, String prenom, BigDecimal salaire, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.salaire = salaire;
        this.adresse = adresse;
    }

    public Salarie(String nom, String prenom, BigDecimal salaire, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.salaire = salaire;
        this.adresse = adresse;
        id = count.incrementAndGet();
    }

    @Override
    public String toString() {
        return "Salarie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", salaire=" + salaire +
                ", adresse='" + adresse + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Salarie)) return false;
        Salarie salarie = (Salarie) o;
        return getId() == salarie.getId() &&
                Objects.equals(getNom(), salarie.getNom()) &&
                Objects.equals(getPrenom(), salarie.getPrenom()) &&
                Objects.equals(getSalaire(), salarie.getSalaire()) &&
                Objects.equals(getAdresse(), salarie.getAdresse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNom(), getPrenom(), getSalaire(), getAdresse());
    }
}
```


 @Entity: pour indiquer qu'il s'agit d'une entité
 @Id: indique que le champ correspondant est un clé primaire
 @GeneratedValue:la clé primaire est autogénérée.

    
  
  
  ## SalariesServiceImpl
  
  Maintenant on va changer nos services pour faire des opérations sur la base de données.
   
  ```
import com.axeane.SpringBootMysql.model.Salarie;
import com.axeane.SpringBootMysql.repositories.SalariesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalariesServiceImpl implements SalariesService {

    private final SalariesRepository salariesRepository;

    public SalariesServiceImpl(SalariesRepository salariesRepository) {
        this.salariesRepository = salariesRepository;
    }

    private Logger logger = LoggerFactory.getLogger(SalariesServiceImpl.class);

    @Override
    public void addsalarie(Salarie salarie) {
        salariesRepository.save(salarie);
    }

    @Override
    public List<Salarie> getListSalaries() {
        return salariesRepository.findAll();
    }

    @Override
    public Salarie findSalariedById(Long searchedId) {
        return salariesRepository.findSalarieById(searchedId);
    }

    @Override
    public void deleteSalaried(Long id) {
        Salarie salarie = findSalariedById(id);
        salariesRepository.delete(salarie);
    }

    @Override
    public void updateSalarie(Salarie salarie) {
        Salarie salarie1 = findSalariedById(salarie.getId());
        if (salarie1 != null) {
            salarie1.setNom(salarie.getNom());
            salarie1.setPrenom(salarie.getPrenom());
            salarie1.setAdresse(salarie.getAdresse());
            salarie1.setSalaire(salarie.getSalaire());
        }
    }

  ```
    
 ## SalariesController
 
 Auccun changement au niveau du contrôleur.
    
 # Test unitaire
 Pour s'assurer de la bonne communication entre Spring et MySQL on a réalisé un test unitaire pour le repository.
 
  ## SalariesRepositoryTest.java
  
 ```
    
import com.axeane.SpringBootMysql.model.Salarie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SalariesRepositoryTest {
    @Autowired
    private SalariesRepository salariesRepository;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testPersistence() {

        Salarie salarie = new Salarie("amira", "raissi", new BigDecimal(444444), "Tunis");
        salariesRepository.save(salarie);
        assertNotNull(salarie.getId());

        Salarie newSalarie = salariesRepository.findSalarieById(salarie.getId());
        assertEquals(salarie.getId(), newSalarie.getId());
        assertEquals(salarie.getNom(), newSalarie.getNom());
        assertEquals(salarie.getPrenom(), newSalarie.getPrenom());
        assertEquals(salarie.getSalaire().compareTo(newSalarie.getSalaire()), 0);
        assertEquals(salarie.getAdresse(), newSalarie.getAdresse());

    }}
   ```
#   II. Mapping One to Many
Après la configuration de MySQL on étudie dans cette partie le mapping des différentes realations entre les tables et on commence par la relation One to Many entre la table Salarie et une nouvelle table departement.





## Departement.java

```

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "department")

public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 256)
    private String nom;
    @NotNull
    @Size(max = 256)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "departement")
    private Set<Salarie> salaries = new HashSet<>();

    public Set<Salarie> getSalaries() {
        return salaries;
    }

    public void setSalaries(Set<Salarie> salaries) {
        this.salaries = salaries;
    }
```



## Salarie.java

```
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "salarie")
public class Salarie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private static final AtomicInteger count = new AtomicInteger(0);

    @NotEmpty
    @NotNull
    private String nom;

    @NotEmpty
    @NotNull
    private String prenom;

    @NotNull
    private BigDecimal salaire;

    @NotEmpty
    @NotNull
    @Size(max = 256, message = "address should have maximum 256 characters")
    private String adresse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Departement departement;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public BigDecimal getSalaire() {
        return salaire;
    }

    public void setSalaire(BigDecimal salaire) {
        this.salaire = salaire;
    }

    public String getAdresse() {
        return adresse;
    }
    @JsonIgnore
    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    @Required
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

}
    
```
    
    
  ##   Repository
  ##      DepartementRepository.java
  ```
  
import com.axeane.SpringBootMysql.model.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartementRepository extends JpaRepository<Departement, Long> {

    List<Departement> findDepartementByNom(String nom);

    Departement findDepartementById(Long id);
}

  ```
  
  
   ##        SalarieRepository.java
   
   ```
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
   ```
   
   
   
   ##    Service
   
   ##       DepartementServiceImpl.java
   
   ```
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

   ```
   
  ##   SalarieServiceImpl.java
 
 ```
 
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
    public Salarie findSalarieById(Long id) {
        return salariesRepository.findSalarieById(id);
    }

 ```
  ## Controllers
      
  ##   DepartementController.java
  
  ```
  
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
  
  ```
  
  ## SalariesController.java
  
  
  ```
  
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
    @GetMapping("/salaries/{id}")
    public ResponseEntity findSalarie(@PathVariable(value = "id") Long id) {
       Salarie salarie =salariesService.findSalarieById(id);

        return new ResponseEntity<>(salarie,HttpStatus.OK);
    }
}
  ```
  
  
  

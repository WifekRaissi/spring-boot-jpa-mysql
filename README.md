 
 
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
   

## Configuration du MySQL 

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
 Pour s'assurer de la bonne communication entre Spring et MySQL on a réalisé des tests d'intégration.
 
 ## Test avec une base de données en mémoire
 
  ## SalariesWithEmbeddedDbTest.java
  
 ``` 
import com.axeane.SpringBootMysql.SpringBootMysqlApplication;
import com.axeane.SpringBootMysql.model.Salarie;
import com.axeane.SpringBootMysql.repositories.SalariesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = SpringBootMysqlApplication.class)
public class SalariesWithEmbeddedDbTest {
    @Autowired
    private SalariesRepository salariesRepository;

    @Test
    public void testWithDb() {
        Salarie salarie = new Salarie("ilyes", "raissi", new BigDecimal(444444), "Tunis");
        Salarie salarie1 = new Salarie("rahma", "raissi", new BigDecimal(55555), "Tunis");
        salariesRepository.save(salarie);
        salariesRepository.save(salarie1);

        assertThat(salarie)
                .matches(c -> Objects.equals(c.getNom(), "ilyes") && Objects.equals(c.getPrenom(), "raissi") && Objects.equals(c.getAdresse(), "Tunis"));

        assertThat(salarie1)
                .matches(c -> Objects.equals(c.getNom(), "rahma") && Objects.equals(c.getPrenom(), "raissi") && Objects.equals(c.getAdresse(), "Tunis"));
        assertThat(salariesRepository.findAll()).containsExactly(salarie, salarie1);
    }
}

 ```
 
L'annotation @DataJpaTest initialise une base de données en mémoire H2 et charge le repository (SalariesRepository) pour le tester.
Mais réellement notre application n'utilise pas H2 donc il faut tester dans un environnement similaire à celui de production.
Pour ceci on a utilisé le testContainer pour tester l'application avec une base de données réelle qui est dans notre cas MySQL ou PostgreSQL.
TestContainer est une librairie Java supportant Junit tests et utilisant les conteneurs Docker.
On lance un conteneur de la base de données pendant seulement la durée du test, ce qui permet de, à la fois, tester dans un environnement réel  et éviter le maintien de la base de données.

##   SalariesWithTestContainer

```

import java.time.Duration;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {SalariesWithTestContainer.Initializer.class})
@SpringBootTest(classes = SpringBootMysqlApplication.class)
@TestPropertySource("/application-test.properties")
public class SalariesWithTestContainer {

    @Autowired
    private SalariesRepository salariesRepository;
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer =
            (PostgreSQLContainer) new PostgreSQLContainer("postgres:9.6.10")
                    .withDatabaseName(" spring")
                    .withUsername("postgres")
                    .withPassword("root")
                    .withStartupTimeout(Duration.ofSeconds(10));

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    public void testWithDbs() {
        Salarie salarie = new Salarie("ilyes", "raissi", "Tunis");
        Salarie salarie1 = new Salarie("rahma", "raissi", "Tunis");
        salariesRepository.save(salarie);
        salariesRepository.save(salarie1);

        assertThat(salarie)
                .matches(c -> Objects.equals(c.getNom(), "ilyes") && Objects.equals(c.getPrenom(), "raissi") && Objects.equals(c.getAdresse(), "Tunis"));

        assertThat(salarie1)
                .matches(c -> Objects.equals(c.getNom(), "rahma") && Objects.equals(c.getPrenom(), "raissi") && Objects.equals(c.getAdresse(), "Tunis"));
        assertThat(salariesRepository.findAll()).containsExactly(salarie, salarie1);
    }
}

```

##  Conclusion
On a continué durant ce tutorial avec Spring Boot l'API Rest en intégrant Spring Data ce qui va être détaillé encore avec l'étude du mapping des différentes relations entre les tables dans les prochains tutoriaux et on commence par la relation One to Many.

https://github.com/WifekRaissi/spring-boot-jpa-mysql-one-to-many

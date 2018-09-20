package com.axeane;

import com.axeane.SpringBootMysql.SpringBootMysqlApplication;
import com.axeane.SpringBootMysql.model.Salarie;
import com.axeane.SpringBootMysql.repositories.SalariesRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(initializers = {SalariesWithTestContainer.Initializer.class})
@SpringBootTest(classes = SpringBootMysqlApplication.class)

public class SalariesWithTestContainer {
    @Autowired
    private SalariesRepository salariesRepository;
 @ClassRule
   public static MySQLContainer mySQLContainer =
            (MySQLContainer) new MySQLContainer("mysql:8.0.12")
                    .withDatabaseName(" testdocker")
                    .withUsername("root")
                    .withPassword("root")
                    .withStartupTimeout(Duration.ofSeconds(10)
                    );

    @Test
    public void testWithDbs() {
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
   public static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + mySQLContainer.getUsername(),
                    "spring.datasource.password=" + mySQLContainer.getPassword()

            ).applyTo(configurableApplicationContext.getEnvironment());

        }}
        }

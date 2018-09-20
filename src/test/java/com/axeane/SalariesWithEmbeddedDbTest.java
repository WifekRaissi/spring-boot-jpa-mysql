package com.axeane;


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
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)

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

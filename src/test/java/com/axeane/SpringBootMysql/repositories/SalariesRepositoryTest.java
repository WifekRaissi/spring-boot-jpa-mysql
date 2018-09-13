package com.axeane.SpringBootMysql.repositories;

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

    }

}
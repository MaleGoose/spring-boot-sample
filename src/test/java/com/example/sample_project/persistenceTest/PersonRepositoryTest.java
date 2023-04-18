package com.example.sample_project.persistenceTest;

import com.example.sample_project.TestFixtures;
import com.example.sample_project.domain.Person;
import com.example.sample_project.domain.Vehicle;
import com.example.sample_project.persistence.PersonRepository;
import com.example.sample_project.persistence.VehicleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    private Vehicle vehicle1;

    private Vehicle vehicle2;

    private Person person1;

    private Person person2;

    @BeforeEach
    void setup(){
        person1 = personRepository.save(TestFixtures.person1());
        person2 = personRepository.save(TestFixtures.person2());
        vehicle1 = vehicleRepository.save(TestFixtures.car1(person1));
        vehicle2 = vehicleRepository.save(TestFixtures.truck1(person2));
        person1.addVehicle(vehicle1);
        person2.addVehicle(vehicle2);
        person1 = personRepository.save(person1);
        person2 = personRepository.save(person2);
        assert person1 != null && person2 != null;
    }

    @Test
    void testSavingPersons(){
        var persons = personRepository.findAll();

        Assertions.assertEquals(2, persons.size());
    }

    @Test
    void testGetPersonByEmail(){
        var person = personRepository.findByEmail("tester@wien5.at");

        Assertions.assertTrue(person.isPresent());
    }

    @Test
    void testBidirectionalRelation(){
        var person = personRepository.findById(person1.getId());

        Assertions.assertTrue(person.isPresent());
        var unwrappedPerson = person.get();
        Assertions.assertNotNull(unwrappedPerson.getVehicles());
        Assertions.assertEquals(Set.of(vehicle1), unwrappedPerson.getVehicles());

    }
}

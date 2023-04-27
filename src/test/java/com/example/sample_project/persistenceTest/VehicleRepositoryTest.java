package com.example.sample_project.persistenceTest;

import com.example.sample_project.TestFixtures;
import com.example.sample_project.domain.Car;
import com.example.sample_project.domain.MotorType;
import com.example.sample_project.domain.Person;
import com.example.sample_project.domain.Truck;
import com.example.sample_project.persistence.PersonRepository;
import com.example.sample_project.persistence.VehicleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PersonRepository personRepository;

    Person person1;

    Person person2;

    @BeforeEach
    void setup(){
        person1 = personRepository.save(TestFixtures.person1());
        person2 = personRepository.save(TestFixtures.person2());
        vehicleRepository.saveAll(TestFixtures.vehicleList(person1, person2));
    }

    @Test
    void testGetTruckByPersonEmail(){
        // when
        var returnedEntity = vehicleRepository.getByPerson_Email("tester@wien5.at");
        var allTrucks = vehicleRepository.findBy();

        // then
        Assertions.assertNotNull(returnedEntity);
        Assertions.assertEquals(1, allTrucks.size());
        Assertions.assertEquals(Truck.class, returnedEntity.getClass());
        Assertions.assertEquals(1250.5, returnedEntity.getStorageVolume());
        Assertions.assertEquals(MotorType.DIESEL, returnedEntity.getMotorType());
        Assertions.assertEquals(person2, returnedEntity.getPerson());

    }

    @Test
    void testGetAllCars() {
        //when
        var returned = vehicleRepository.findAllBy();

        //then
        Assertions.assertNotNull(returned);
        Assertions.assertEquals(1, returned.size());
        Assertions.assertEquals(Car.class, returned.get(0).getClass());
        Assertions.assertEquals(person1, returned.get(0).getPerson());
    }

    @Test
    void testFindAllByPerson_Name_LastNameContainingAndMotorTypeOrderByHorsePowerDesc(){
        var returned = vehicleRepository.findAllByPerson_Name_LastNameContainingAndMotorTypeOrderByHorsePowerDesc("s",MotorType.DIESEL);

        Assertions.assertEquals(2,returned.size());
        Assertions.assertEquals(200L,returned.get(0).horsePower());
    }

    @Test
    void testNameAndHorsePowerView(){
        var returned = vehicleRepository.nameAndHorsePowerView("s",MotorType.DIESEL);

        Assertions.assertEquals(2,returned.size());
        Assertions.assertEquals(200L,returned.get(0).horsePower());
    }
}

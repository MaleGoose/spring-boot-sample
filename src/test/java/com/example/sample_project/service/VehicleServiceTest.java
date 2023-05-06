package com.example.sample_project.service;


import com.example.sample_project.TestFixtures;
import com.example.sample_project.domain.Car;
import com.example.sample_project.domain.Person;
import com.example.sample_project.persistence.VehicleRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private PersonService personService;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    public void testCoolServiceMethod(){
        Person person1 = TestFixtures.person1();
        Person person2 = TestFixtures.person2();
        Car car1 = TestFixtures.car1(person2);
        Car edit_car1 = TestFixtures.car1(person1);
        when(personService.getPersonByEmail(any())).thenReturn(Optional.of(TestFixtures.person2())).thenReturn(Optional.of(person1));
        when(vehicleRepository.findById(any())).thenReturn(Optional.of(car1));
        when(vehicleRepository.save(any())).thenReturn(car1).thenReturn(edit_car1);

        var vehicle = vehicleService.changeOwnership(101L, TestFixtures.transferOwnershipCommand1());

        Assertions.assertEquals(vehicle.getPerson(), person1);

    }

}
package com.example.sample_project.service;

import com.example.sample_project.TestFixtures;
import com.example.sample_project.persistence.PersonRepository;
import com.example.sample_project.persistence.VehicleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class VehicleServiceTest {

    @MockBean
    private VehicleRepository vehicleRepository;
    @MockBean
    private PersonRepository personRepository;

    private VehicleService vehicleService;
    private PersonService personService;

    @BeforeEach
    void setup() {
        personService = new PersonService(personRepository);
        vehicleService = new VehicleService(vehicleRepository, personService);
    }

}
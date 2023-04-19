package com.example.sample_project.service;

import com.example.sample_project.domain.*;
import com.example.sample_project.persistence.VehicleRepository;
import com.example.sample_project.service.dto.NameStatistic;
import com.example.sample_project.service.dto.VehicleCommand;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final PersonService personService;

    @Transactional
    public Vehicle createNewVehicleWithTypeAndConnectionToPerson(String personEmail, VehicleType type, VehicleCommand command){
        var person = personService.getPersonByEmail(personEmail)
                .orElseThrow(() -> new NullPointerException("No Person with this Email Address found!"));
        Vehicle vehicle;
        switch (type){
            case CAR -> vehicle = Car.builder()
                    .horsePower(command.getHorsePower())
                    .person(person)
                    .motorType(command.getMotorType())
                    .hasTrunk(command.isHasTrunk())
                    .build();
            case TRUCK -> vehicle = Truck.builder()
                    .horsePower(command.getHorsePower())
                    .person(person)
                    .motorType(command.getMotorType())
                    .storageVolume(command.getStorageVolume())
                    .build();
            default -> throw new IllegalArgumentException("Invalid Type found!");
        }
        vehicle = vehicleRepository.save(vehicle);
        person.addVehicle(vehicle);
        vehicle = vehicleRepository.save(vehicle);
        return vehicle;
    }

    public NameStatistic createNameStatistic(String containedInLastName, MotorType motorType){
        var listOfVehicles = vehicleRepository.findAllByPerson_Name_LastNameContainingAndMotorTypeOrderByHorsePowerDesc(containedInLastName, motorType);

        return null;
    }

    @Transactional
    public boolean changeOwnership(Long vehicleId, Long personId){

        return true;
    }
}

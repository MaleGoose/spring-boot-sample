package com.example.sample_project.service;

import com.example.sample_project.domain.*;
import com.example.sample_project.persistence.VehicleRepository;
import com.example.sample_project.persistence.projection.NameAndHorsepowerView;
import com.example.sample_project.service.dto.NameStatistic;
import com.example.sample_project.service.dto.TransferOwnershipCommand;
import com.example.sample_project.service.dto.VehicleCommand;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final PersonService personService;

    @Transactional
    public Vehicle createNewVehicleWithTypeAndConnectionToPerson(String personEmail, VehicleCommand command){
        var person = personService.getPersonByEmail(personEmail)
                .orElseThrow(() -> new NullPointerException("No Person with this Email Address found!"));
        Vehicle vehicle;
        switch (command.getType()){
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
        List<NameAndHorsepowerView> listOfVehicles = vehicleRepository.findAllByPerson_Name_LastNameContainingAndMotorTypeOrderByHorsePowerDesc(containedInLastName, motorType);
        List<NameAndHorsepowerView> bestVehicles = listOfVehicles.stream().limit(3).collect(Collectors.toList());
        return new NameStatistic(containedInLastName, bestVehicles, motorType);
    }

    @Transactional
    public Vehicle changeOwnership(Long vehicleId, TransferOwnershipCommand command){
        Person fromPerson = personService.getPersonByEmail(command.getFrom())
                .orElseThrow(() -> new IllegalArgumentException("No From-Person with this Email found!"));
        Person toPerson = personService.getPersonByEmail(command.getTo())
                .orElseThrow(() -> new IllegalArgumentException("No To-Person with this Email found!"));
        Vehicle transferedVehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("No Transfer-Vehicle with this ID found!"));

        fromPerson.removeVehicle(transferedVehicle);
        transferedVehicle = vehicleRepository.save(transferedVehicle);
        toPerson.addVehicle(transferedVehicle);
        transferedVehicle.setPerson(toPerson);
        transferedVehicle = vehicleRepository.save(transferedVehicle);
        return transferedVehicle;
    }
}

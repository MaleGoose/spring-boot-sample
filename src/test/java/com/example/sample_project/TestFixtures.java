package com.example.sample_project;

import com.example.sample_project.domain.*;
import com.example.sample_project.service.dto.TransferOwnershipCommand;
import com.example.sample_project.service.dto.VehicleCommand;

import java.util.List;

public class TestFixtures {

    public static Person person1(){
        return Person.builder()
                .name(new Name("Spengergasse", "Tester"))
                .email("test@spengergasse.at")
                .build();
    }

    public static Person person2(){
        return Person.builder()
                .name(new Name("Wien 5", "Tester"))
                .email("tester@wien5.at")
                .build();
    }

    public static List<Person> personList(){
        return List.of(person1(), person2());
    }

    public static Car car1(Person person){
        return Car.builder()
                .horsePower(200L)
                .motorType(MotorType.DIESEL)
                .hasTrunk(true)
                .person(person)
                .build();
    }

    public static Truck truck1(Person person){
        return Truck.builder()
                .horsePower(200L)
                .motorType(MotorType.DIESEL)
                .storageVolume(1250.5)
                .person(person)
                .build();
    }

    public static TransferOwnershipCommand transferOwnershipCommand1(){
        return new TransferOwnershipCommand("from@gmail.com", "to@gmail,com");

    }

    public static VehicleCommand vehicleCommand1() {
        return new VehicleCommand(VehicleType.TRUCK, MotorType.ELECTRIC, 150L, false, 50.0);
    }

    public static List<Vehicle> vehicleList(Person person1, Person person2){
        return List.of(car1(person1), truck1(person2));
    }
}

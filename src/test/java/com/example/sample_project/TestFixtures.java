package com.example.sample_project;

import com.example.sample_project.domain.*;

public class TestFixtures {

    public static Person person1(){
        return Person.builder()
                .name(new Name("Spengergasse", "Tester"))
                .email("test@spengergasse.at")
                .build();
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
}

package com.example.sample_project.persistence;

import com.example.sample_project.domain.Car;
import com.example.sample_project.domain.MotorType;
import com.example.sample_project.domain.Truck;
import com.example.sample_project.domain.Vehicle;
import com.example.sample_project.persistence.projection.NameAndHorsepowerView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Truck getByPerson_Email(String email);
    List<Truck> findBy();
    List<Car> findAllBy();

    List<NameAndHorsepowerView> findAllByPerson_Name_LastNameContainingAndMotorTypeOrderByHorsePowerDesc(String containedInLastName, MotorType motorType);
}

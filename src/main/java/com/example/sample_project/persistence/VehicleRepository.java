package com.example.sample_project.persistence;

import com.example.sample_project.domain.Car;
import com.example.sample_project.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Car getByPerson_Email(String email);
}

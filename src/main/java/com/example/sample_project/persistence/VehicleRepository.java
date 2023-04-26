package com.example.sample_project.persistence;

import com.example.sample_project.domain.*;
import com.example.sample_project.persistence.projection.NameAndHorsepowerView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Truck getByPerson_Email(String email);
    List<Truck> findBy();
    List<Car> findAllBy();

    List<NameAndHorsepowerView> findAllByPerson_Name_LastNameContainingAndMotorTypeOrderByHorsePowerDesc(String containedInLastName, MotorType motorType);

    //Same Query but with SQL @Query Annotation
    @Query("SELECT NEW  com.example.sample_project.persistence.projection.NameAndHorsepowerView(v.person.name, v.horsePower) " +
            "FROM Vehicle v " +
            "WHERE v.person.name.lastName LIKE %:containedInLastName% " +
            "AND v.motorType = :motorType " +
            "ORDER BY v.horsePower DESC")
    List<NameAndHorsepowerView> nameAndHorsePowerView(@Param("containedInLastName")String containedInLastName, @Param("motorType") MotorType motorType);
}

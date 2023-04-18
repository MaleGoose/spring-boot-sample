package com.example.sample_project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Person extends AbstractPersistable<Long> {

    @Embedded
    @Column(unique = true, nullable = false)
    private Name name;

    @Email
    private String email;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private Set<Vehicle> vehicles = new HashSet<>();

    public void addVehicle(Vehicle vehicle){
        assert vehicle != null;
        var vehicleSet = this.vehicles;
        if(vehicleSet == null)
            vehicleSet = new HashSet<>();
        vehicleSet.add(vehicle);
        this.vehicles = vehicleSet;
    }

    public void removeVehicle(Vehicle vehicle){
        assert vehicle != null;
        var vehicleSet = this.vehicles;
        if(vehicleSet == null)
            vehicleSet = new HashSet<>();
        vehicleSet.remove(vehicle);
        this.vehicles = vehicleSet;
    }
}
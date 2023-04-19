package com.example.sample_project.service.dto;

import com.example.sample_project.domain.MotorType;
import com.example.sample_project.domain.VehicleType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleCommand {

    // General Vehicle
    private VehicleType type;
    private MotorType motorType;
    private Long horsePower;

    // Car or Truck specific fields
    private boolean hasTrunk;
    private Double storageVolume;
}

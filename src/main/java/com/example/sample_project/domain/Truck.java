package com.example.sample_project.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@DiscriminatorValue("T")
public class Truck extends Vehicle{

    private Double storageVolume;
}
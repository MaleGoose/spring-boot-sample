package com.example.sample_project.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@DiscriminatorValue("C")
public class Car extends Vehicle{

    private boolean hasTrunk;
}

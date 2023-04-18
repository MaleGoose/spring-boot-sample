package com.example.sample_project.service.DTO;

import com.example.sample_project.domain.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NameStatistic {

    private String queriedLastName;

    private List<Vehicle> topThree;

    private String vehicleType;
}

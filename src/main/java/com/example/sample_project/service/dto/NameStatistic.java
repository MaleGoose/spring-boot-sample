package com.example.sample_project.service.dto;

import com.example.sample_project.domain.MotorType;
import com.example.sample_project.domain.Vehicle;
import com.example.sample_project.persistence.projection.NameAndHorsepowerView;
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

    private List<NameAndHorsepowerView> topThree;

    private MotorType motorType;
}

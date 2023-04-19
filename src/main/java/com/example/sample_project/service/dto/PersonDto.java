package com.example.sample_project.service.dto;

import com.example.sample_project.domain.Person;
import com.example.sample_project.domain.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private String firstName;
    private String lastName;
    private String email;
    private List<Vehicle> vehicles;

    public static PersonDto from(Person p) {
        return PersonDto.builder()
                .firstName(p.getName().getFirstName())
                .lastName(p.getName().getLastName())
                .email(p.getEmail())
                .vehicles(p.getVehicles().stream().toList())
                .build();
    }
}

package com.example.sample_project.presentation;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(VehicleController.PATH)
@AllArgsConstructor
public class VehicleController {

    public final static String PATH = "/api/vehicle";

    @PutMapping("/ownership")
    public void changeOwnership(){
    }

}
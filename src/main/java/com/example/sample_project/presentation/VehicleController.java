package com.example.sample_project.presentation;

import com.example.sample_project.domain.Vehicle;
import com.example.sample_project.service.VehicleService;
import com.example.sample_project.service.dto.TransferOwnershipCommand;
import com.example.sample_project.service.dto.VehicleCommand;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(VehicleController.PATH)
@AllArgsConstructor
public class VehicleController {

    public final static String PATH = "/api/vehicle";
    private final VehicleService  vehicleService;

    @PutMapping("/{vehicleId}/ownership")
    public HttpEntity<Vehicle> changeOwnership(@RequestBody TransferOwnershipCommand cmd, @PathVariable Long vehicleId) {
        var returned = vehicleService.changeOwnership(vehicleId, cmd);

        return ResponseEntity.created(URI.create("%s/%s".formatted(PATH, vehicleId))).body(returned);
    }

    @PostMapping("/{personMail}")
    public HttpEntity<Vehicle> createVehicle(@RequestBody VehicleCommand cmd, @PathVariable String personMail) {
        var returned = vehicleService.createNewVehicleWithTypeAndConnectionToPerson(personMail, cmd);

        return ResponseEntity.created(URI.create("%s/%s".formatted(PATH, returned.getId()))).body(returned);
    }

}
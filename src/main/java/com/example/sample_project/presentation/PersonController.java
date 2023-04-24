package com.example.sample_project.presentation;

import com.example.sample_project.service.PersonService;
import com.example.sample_project.service.dto.PersonDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PersonController.PATH)
@AllArgsConstructor
public class PersonController {

    public final static String PATH = "/api/person";
    private final PersonService personService;

    @GetMapping("/{email}")
    public HttpEntity<PersonDto> findByEmail(@PathVariable String email) {
        return personService.getPersonByEmail(email).map(PersonDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/horsepower")
    public HttpEntity<Void> deleteByMaxHorsePowerSum() {
        var returned = personService.findPersonWithMostHorsepowerInSumAndDelete();

        return returned ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }

}

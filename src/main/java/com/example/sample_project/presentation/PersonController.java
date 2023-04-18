package com.example.sample_project.presentation;

import com.example.sample_project.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PersonController.PATH)
@AllArgsConstructor
public class PersonController {

    public final static String PATH = "/api/person";
    private final PersonService personService;

}

package com.example.sample_project.presentation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PersonController.PATH)
public class PersonController {

    public final static String PATH = "/api/person";


}

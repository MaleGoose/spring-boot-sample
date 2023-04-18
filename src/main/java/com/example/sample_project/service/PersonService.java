package com.example.sample_project.service;

import com.example.sample_project.domain.Person;
import com.example.sample_project.persistence.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;


    public Optional<Person> getPersonByEmail(String email){
        return personRepository.findByEmail(email);
    }

    public boolean findPersonWithMostHorsepowerInSumAndDelete(){
        var persons = personRepository.findAll();
        Person highestHorsepower;

        return false;
    }
}

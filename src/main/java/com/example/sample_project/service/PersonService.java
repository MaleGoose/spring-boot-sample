package com.example.sample_project.service;

import com.example.sample_project.domain.Person;
import com.example.sample_project.domain.Vehicle;
import com.example.sample_project.persistence.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;


    public Optional<Person> getPersonByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    @Transactional
    public boolean findPersonWithMostHorsepowerInSumAndDelete() {
        List<Person> persons = personRepository.findAll();
        if (persons.isEmpty())
            return false;

        List<PersonAndSum> personAndSums = persons.stream().map(PersonAndSum::new).toList();
        PersonAndSum highestPerson = Collections.max(personAndSums, Comparator.comparing(PersonAndSum::getSumOfHorsePower));
        try {
            personRepository.delete(highestPerson.getPerson());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Getter
    public static class PersonAndSum {
        private final Person person;
        private final Long sumOfHorsePower;

        public PersonAndSum(Person person) {
            this.person = person;
            this.sumOfHorsePower = person.getVehicles().stream().collect(Collectors.summarizingLong(Vehicle::getHorsePower)).getSum();
        }
    }
}

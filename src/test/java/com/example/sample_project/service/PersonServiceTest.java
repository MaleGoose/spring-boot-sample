package com.example.sample_project.service;

import com.example.sample_project.TestFixtures;
import com.example.sample_project.domain.Person;
import com.example.sample_project.persistence.PersonRepository;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    private PersonService personService;
    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    void setup() {
        personService = new PersonService(personRepository);
    }

    @Test
    void ensureDeleteWithMostHorsePowerWorks() {
        //given
        when(personRepository.findAll()).thenReturn(TestFixtures.personList());

        //when
        boolean success = personService.findPersonWithMostHorsepowerInSumAndDelete();

        //then
        Assertions.assertTrue(success);
    }

    @Test
    void ensureDeletingWithoutEntitiesInRepoReturnsFalse() {
        //given
        when(personRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        boolean success = personService.findPersonWithMostHorsepowerInSumAndDelete();

        //then
        assertFalse(success);
    }

    @Test
    void ensureDeletingReturnsFalseWhenPersistenceExceptionIsThrown() {
        //given
        when(personRepository.findAll()).thenReturn(TestFixtures.personList());
        doThrow(new PersistenceException()).when(personRepository).delete(ArgumentMatchers.any());

        //when
        boolean success = personService.findPersonWithMostHorsepowerInSumAndDelete();

        //then
        assertFalse(success);
    }

}
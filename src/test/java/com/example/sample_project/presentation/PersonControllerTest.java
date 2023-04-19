package com.example.sample_project.presentation;

import com.example.sample_project.domain.Name;
import com.example.sample_project.domain.Person;
import com.example.sample_project.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
public class PersonControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    private static final String PATH = "/api/person";
    private static final String PATH_GET_EMAIL = "/mail@example.com";

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PersonController(personService)).build();
    }

    @Test
    void ensureFindByMailValidWorks() throws Exception {
        //given
        Person person = Person.builder().email("mail@example.com").name(Name.builder().firstName("Max").lastName("Müller").build()).build();
        when(personService.getPersonByEmail(ArgumentMatchers.any())).thenReturn(Optional.of(person));

        //when
        mockMvc.perform(get(PATH + PATH_GET_EMAIL).accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(person.getName().getFirstName()))
                .andExpect(jsonPath("$.lastName").value(person.getName().getLastName()))
                .andExpect(jsonPath("$.email").value(person.getEmail()));
    }

    @Test
    void ensureFindByEmailInvalidRetursNotFound() throws Exception {
        //given
        when(personService.getPersonByEmail(ArgumentMatchers.any())).thenReturn(Optional.empty());

        //when
        mockMvc.perform(get(PATH + PATH_GET_EMAIL).accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePerson() throws Exception {

        mockMvc.perform(post("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print());

    }
}

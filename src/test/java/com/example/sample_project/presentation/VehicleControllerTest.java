package com.example.sample_project.presentation;

import com.example.sample_project.TestFixtures;
import com.example.sample_project.service.VehicleService;
import com.example.sample_project.service.dto.TransferOwnershipCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
class VehicleControllerTest {

    @MockBean
    private VehicleService vehicleService;
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private final String PATH = "/api/vehicle";
    private final String CHANGE_OWNER = "/%s/ownership";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new VehicleController(vehicleService)).build();
    }

    @Test
    void ensureChangingOwnershipWorks() throws Exception {
        //given
        when(vehicleService.changeOwnership(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(TestFixtures.car1(TestFixtures.person1()));

        String json = mapper.writeValueAsString(new TransferOwnershipCommand("abc@gmail.com", "def@gmail.com"));

        //when
        mockMvc.perform(put(PATH + CHANGE_OWNER.formatted(123))
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.motorType").value("DIESEL"))
                .andExpect(jsonPath("$.horsePower").value(200))
                .andExpect(jsonPath("$.person.name.lastName").value("Tester"));
    }

    @Test
    void ensureChangingOwnershipWithoutBodyReturns400() throws Exception {
        //given
        when(vehicleService.changeOwnership(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(TestFixtures.car1(TestFixtures.person1()));

        //when
        mockMvc.perform(put(PATH + CHANGE_OWNER.formatted(123))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ensureCreatingVehicleWorks() throws Exception {
        //given
        when(vehicleService.createNewVehicleWithTypeAndConnectionToPerson(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(TestFixtures.truck1(TestFixtures.person2()));

        String json = mapper.writeValueAsString(TestFixtures.vehicleCommand1());

        String CREATE_EMAIL = "/%s";
        mockMvc.perform(post(PATH + CREATE_EMAIL.formatted("abc@example.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.storageVolume").value(1250.5));
    }


}
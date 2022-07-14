package ohara.training.spring.boot.lab.domain.person.controllers;


import ohara.training.spring.boot.lab.domain.person.BaseControllerTest;
import ohara.training.spring.boot.lab.domain.person.exceptions.PersonNotFoundException;
import ohara.training.spring.boot.lab.domain.person.models.Person;
import ohara.training.spring.boot.lab.domain.person.models.PhoneNumber;
import ohara.training.spring.boot.lab.domain.person.services.PersonService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class PersonControllerTest extends BaseControllerTest {

    @MockBean
    private PersonService mockPersonService;

    @Autowired
    private MockMvc mockMvc;

    private Person inputPerson;
    private Person mockResponsePerson1;
    private Person mockResponsePerson2;

    @BeforeEach
    public void setUp() {
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(new PhoneNumber("1234567890", true));
        phoneNumbers.add(new PhoneNumber("0987654321", false));
        inputPerson = new Person("bob", "smith", phoneNumbers);

        mockResponsePerson1 = new Person("person", "last", phoneNumbers);
        mockResponsePerson1.setId(1);
        mockResponsePerson2 = new Person("first", "person", phoneNumbers);
        mockResponsePerson2.setId(2);
    }

    @Test
    @DisplayName("Person post: /people - success")
    public void createPersonRequestSuccess() throws Exception {
        BDDMockito.doReturn(mockResponsePerson1).when(mockPersonService).create(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockResponsePerson1)))

                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Is.is(1)))
                .andExpect(jsonPath("$.firstName", Is.is("person")))
                .andExpect(jsonPath("$.lastName", Is.is("last")));
    }

    @Test
    @DisplayName("GET /people/1 - Found")
    public void getPersonByIdTestSuccess() throws Exception {
        BDDMockito.doReturn(mockResponsePerson1).when(mockPersonService).getPersonById(1);

        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Is.is(1)))
                .andExpect(jsonPath("$.firstName", Is.is("person")))
                .andExpect(jsonPath("$.lastName", Is.is("last")));
    }

    @Test
    @DisplayName("GET /people/1 - Not found")
    public void getPersonByIdTestFail() throws Exception {
        BDDMockito.doThrow(new PersonNotFoundException("Not Found")).when(mockPersonService).getPersonById(1);
        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("PUT /people/1 - Success")
    public void putPersonTestSuccess() throws Exception {
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(new PhoneNumber("3456432345", true));
        phoneNumbers.add(new PhoneNumber("7896534567", true));
        Person expectedPersonUpdate = new Person("name after", "update", phoneNumbers);
        expectedPersonUpdate.setId(1);
        BDDMockito.doReturn(expectedPersonUpdate).when(mockPersonService).updatePerson(any(), any());
        mockMvc.perform(MockMvcRequestBuilders.put("/people/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedPersonUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Is.is(1)))
                .andExpect(jsonPath("$.firstName", Is.is("name after")))
                .andExpect(jsonPath("$.lastName", Is.is("update")));
    }

    @Test
    @DisplayName("PUT /people/1 - NOt found")
    public void putPersonTestNotFound() throws Exception {
        BDDMockito.doThrow(new PersonNotFoundException("Not Found")).when(mockPersonService).updatePerson(any(), any());
        mockMvc.perform(MockMvcRequestBuilders.put("/people/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inputPerson)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /people/1 - Success")
    public void deletePersonTestSuccess() throws Exception {
        BDDMockito.doReturn(true).when(mockPersonService).deletePerson(any());
        mockMvc.perform(MockMvcRequestBuilders.delete("/people/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /people/1 - not found")
    public void deletePersonNotFoundTest() throws Exception {
        BDDMockito.doThrow(new PersonNotFoundException("Not Found")).when(mockPersonService).deletePerson(any());
        mockMvc.perform(MockMvcRequestBuilders.delete("/people/{id}", 1))
                .andExpect(status().isNotFound());
    }

}


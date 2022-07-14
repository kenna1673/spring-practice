package ohara.training.spring.boot.lab.domain.person.services;

import ohara.training.spring.boot.lab.domain.person.exceptions.PersonNotFoundException;
import ohara.training.spring.boot.lab.domain.person.models.Person;
import ohara.training.spring.boot.lab.domain.person.models.PhoneNumber;
import ohara.training.spring.boot.lab.domain.person.repos.PersonRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PersonServiceTest {

    @MockBean
    private PersonRepo mockPersonRepo;

    @Autowired
    private PersonService personService;

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
    @DisplayName("Person Service: Createe Person - Success")
    public void createPersonTestSuccess() {
        BDDMockito.doReturn(mockResponsePerson1).when(mockPersonRepo).save(ArgumentMatchers.any());
        Person returnedPerson = personService.create(inputPerson);
        Assertions.assertNotNull(returnedPerson, "Person should not be null");
        Assertions.assertEquals(returnedPerson.getId(), 1);
    }

    @Test
    @DisplayName("Person Service: Get Person By Id - Success")
    public void getPersonByIdTestSuccess() throws PersonNotFoundException {
        BDDMockito.doReturn(Optional.of(mockResponsePerson1)).when(mockPersonRepo).findById(1);
        Person foundPerson = personService.getPersonById(1);
        Assertions.assertEquals(mockResponsePerson1.toString(), foundPerson.toString());
    }

    @Test
    @DisplayName("Person Service: Get Person by Id - Fail")
    public void getPersonByIdTestFailed() {
        BDDMockito.doReturn(Optional.empty()).when(mockPersonRepo).findById(1);
        Assertions.assertThrows(PersonNotFoundException.class, () -> {
            personService.getPersonById(1);
        });
    }

    @Test
    @DisplayName("Person Service: Get all People - Success")
    public void getAllPeopleTestSuccess() {
        List<Person> people = new ArrayList<>();
        people.add(mockResponsePerson1);
        people.add(mockResponsePerson2);

        BDDMockito.doReturn(people).when(mockPersonRepo).findAll();

        List<Person> responsePeople = personService.getAllPeople();
        Assertions.assertIterableEquals(people, responsePeople);
    }

    @Test
    @DisplayName("Person Service: Update Person - Success")
    public void updatePersonTestSuccess() throws PersonNotFoundException {
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(new PhoneNumber("3456432345", true));
        phoneNumbers.add(new PhoneNumber("7896534567", true));
        Person expectedPersonUpdate = new Person("name after", "update", phoneNumbers);

        BDDMockito.doReturn(Optional.of(mockResponsePerson1)).when(mockPersonRepo).findById(1);
        BDDMockito.doReturn(expectedPersonUpdate).when(mockPersonRepo).save(ArgumentMatchers.any());

        Person actualPerson = personService.updatePerson(1, expectedPersonUpdate);
        Assertions.assertEquals(expectedPersonUpdate, actualPerson);
    }

    @Test
    @DisplayName("Person Service: Update Person - fail")
    public void updatePersonTestFail() {
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(new PhoneNumber("3456432345", true));
        phoneNumbers.add(new PhoneNumber("7896534567", true));
        Person expectedPersonUpdate = new Person("name after", "update", phoneNumbers);
        BDDMockito.doReturn(Optional.empty()).when(mockPersonRepo).findById(1);
        Assertions.assertThrows(PersonNotFoundException.class, () -> {
            personService.updatePerson(1, expectedPersonUpdate);
        });
    }

    @Test
    @DisplayName("Person Service: Delete Person - Success")
    public void deletePersonTestSuccess() throws PersonNotFoundException {
        BDDMockito.doReturn(Optional.of(mockResponsePerson1)).when(mockPersonRepo).findById(1);
        Boolean actualResponse = personService.deletePerson(1);
        Assertions.assertTrue(actualResponse);
    }

    @Test
    @DisplayName("Person service: delete person - fail")
    public void deletePersonTestFail() {
        BDDMockito.doReturn(Optional.empty()).when(mockPersonRepo).findById(1);
        Assertions.assertThrows(PersonNotFoundException.class, () -> {
            personService.deletePerson(1);
        });
    }
}

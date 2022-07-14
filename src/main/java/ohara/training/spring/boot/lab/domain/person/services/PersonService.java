package ohara.training.spring.boot.lab.domain.person.services;

import ohara.training.spring.boot.lab.domain.person.exceptions.PersonNotFoundException;
import ohara.training.spring.boot.lab.domain.person.models.Person;

import java.util.List;

public interface PersonService {
    Person create(Person person);
    Person getPersonById(Integer id) throws PersonNotFoundException;
    List<Person> getAllPeople();
    Person updatePerson(Integer id, Person person) throws PersonNotFoundException;
    Boolean deletePerson(Integer id) throws PersonNotFoundException;
}

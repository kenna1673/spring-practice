package ohara.training.spring.boot.lab.domain.person.services;

import ohara.training.spring.boot.lab.domain.person.exceptions.PersonNotFoundException;
import ohara.training.spring.boot.lab.domain.person.models.Person;
import ohara.training.spring.boot.lab.domain.person.repos.PersonRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private static Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
    private PersonRepo personRepo;

    @Autowired
    public PersonServiceImpl(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }


    @Override
    public Person create(Person person) {
        return personRepo.save(person);
    }

    @Override
    public Person getPersonById(Integer id) throws PersonNotFoundException {
        Optional<Person> personOptional = personRepo.findById(id);
        if (personOptional.isEmpty()) {
            logger.error("Person with id {} does not exist", id);
            throw new PersonNotFoundException("Person not found");
        }
        return personOptional.get();
    }

    @Override
    public List<Person> getAllPeople() {
        return personRepo.findAll();
    }

    @Override
    public Person updatePerson(Integer id, Person person) throws PersonNotFoundException {
        Optional<Person> personOptional = personRepo.findById(id);
        if (personOptional.isEmpty()) {
            throw new PersonNotFoundException("Person does not exist, can not update");
        }
        Person savedPerson = personOptional.get();
        savedPerson.setFirstName(person.getFirstName());
        savedPerson.setLastName(person.getLastName());
        savedPerson.setPhoneNumbers(person.getPhoneNumbers());

        return personRepo.save(savedPerson);
    }

    @Override
    public Boolean deletePerson(Integer id) throws PersonNotFoundException {
        Optional<Person> personOptional = personRepo.findById(id);
        if (personOptional.isEmpty()) {
            throw new PersonNotFoundException("Person does not exist, can not delete");
        }
        Person personToDelete = personOptional.get();
        personRepo.delete(personToDelete);
        return true;
    }
}

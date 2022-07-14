package ohara.training.spring.boot.lab.domain.person.repos;

import ohara.training.spring.boot.lab.domain.person.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepo extends JpaRepository<Person, Integer> {
    List<Person> findByLastName(String name);
}

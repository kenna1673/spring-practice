package ohara.training.spring.boot.lab.domain.person.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    private Person person1;
    private Person person2;
    private Person emptyPerson1;
    private Person emptyPerson2;

    @BeforeEach
    public void setUp() {
        emptyPerson1 = new Person();
        emptyPerson2 = new Person();

        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(new PhoneNumber("1234567890", true));
        phoneNumbers.add(new PhoneNumber("0987654321", false));

        person1 = new Person("John", "tom", phoneNumbers);
        person1.setId(1);
        person2 = new Person("Bob", "smith", phoneNumbers);
        person2.setId(2);
    }

    @Test
    public void testEmptyEquals() throws Exception {
        Assertions.assertTrue(emptyPerson1.equals(emptyPerson2),
                "Both empty people instances should be equal"
        );
    }

    @Test
    public void testContentEquals() throws Exception {
        Assertions.assertTrue(person1.equals(person2),
                "Both non-empty people instances should be equal");
    }

    @Test
    public void testNotEquals() throws Exception {

        assertFalse(
                person1.equals(person2),
                "The Person instances should not be equal");
    }

    @Test
    public void testEmptyHashCode() throws Exception {

        assertEquals(
                emptyPerson1.hashCode(),
                emptyPerson2.hashCode(),
                "Both empty Widget instances should have the same hashCode");
    }

    @Test
    public void testContentHashCode() throws Exception {

        assertEquals(
                person1.hashCode(),
                person2.hashCode(),
                "Both non-empty Person instances should have the same hashCode");
    }

    @Test
    public void testHashCode() throws Exception {

        assertNotEquals(
                emptyPerson1.hashCode(),
                person2.hashCode(),
                "The Person instances should not have the same hashCode");
    }

    @Test
    public void testEmptyToString() throws Exception {

        assertEquals(
                emptyPerson1.toString(),
                emptyPerson2.toString(),
                "Both empty Person instances should have the same toString");
    }

    @Test
    public void testContentToString() throws Exception {

        assertEquals(
                person1.toString(),
                person2.toString(),
                "Both non-empty Person instances should have the same toString");
    }

    @Test
    public void testNotToString() throws Exception {

        assertNotEquals(
                emptyPerson1.toString(),
                person2.toString(),
                "The Person instances should not have the same toString");
    }
}

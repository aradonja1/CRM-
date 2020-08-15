package ba.unsa.etf.rpr.projekat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void testToString() {
        Person person = new Person("Ime", "Prezime");
        assertEquals("Ime Prezime", person.toString());
    }
}
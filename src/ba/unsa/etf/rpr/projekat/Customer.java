package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class Customer extends Person {

    public Customer(String name, String surname, String umcn, String contact, String email, LocalDate dateOfBirth) {
        super(name, surname, umcn, contact, email, dateOfBirth);
    }
}

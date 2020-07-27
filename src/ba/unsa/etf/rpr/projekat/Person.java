package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class Person {
    private String name, surname, umcn, contact, email;
    private LocalDate dateOfBirth;

    public Person(String name, String surname, String umcn, String contact, String email, LocalDate dateOfBirth) {
        this.name = name;
        this.surname = surname;
        this.umcn = umcn;
        this.contact = contact;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUmcn() {
        return umcn;
    }

    public void setUmcn(String umcn) {
        this.umcn = umcn;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}

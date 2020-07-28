package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class Customer {
    private String name, surname, email, adress, contact;
    private LocalDate beginContract, endContract;
    private Service service;

    public Customer(String name, String surname, String email, String adress, String contact, LocalDate beginContract, LocalDate endContract, Service service) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.adress = adress;
        this.contact = contact;
        this.beginContract = beginContract;
        this.endContract = endContract;
        this.service = service;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDate getBeginContract() {
        return beginContract;
    }

    public void setBeginContract(LocalDate beginContract) {
        this.beginContract = beginContract;
    }

    public LocalDate getEndContract() {
        return endContract;
    }

    public void setEndContract(LocalDate endContract) {
        this.endContract = endContract;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}

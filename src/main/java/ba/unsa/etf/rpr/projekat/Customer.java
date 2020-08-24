package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;
import java.util.ArrayList;

public class Customer extends Person {
    private int id;
    private String email, adress, contact;
    private LocalDate beginContract, endContract;
    private Service service;
    private ArrayList<Contract> contracts = new ArrayList<>();

    public Customer(int id, String firstName, String lastName, String email, String adress, String contact, LocalDate beginContract, LocalDate endContract, Service service, ArrayList<Contract> contracts) {
        super(firstName, lastName);
        this.id = id;
        this.email = email;
        this.adress = adress;
        this.contact = contact;
        this.beginContract = beginContract;
        this.endContract = endContract;
        this.service = service;
        this.contracts = contracts;
    }

    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return super.getFirstName();
    }

    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    public String getLastName() {
        return super.getLastName();
    }

    public void setLastName(String lastName) {
        super.setLastName(lastName);
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

    public ArrayList<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(ArrayList<Contract> contracts) {
        this.contracts = contracts;
    }

}

package ba.unsa.etf.rpr.projekat.DAL;

import ba.unsa.etf.rpr.projekat.Contract;
import ba.unsa.etf.rpr.projekat.Customer;
import ba.unsa.etf.rpr.projekat.Package;
import ba.unsa.etf.rpr.projekat.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDAOTest {

    @BeforeEach
    void regenerisiBazu() {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();
    }

    @Test
    void customers() throws SQLException{
        CustomerDAO c = new CustomerDAO();
        ArrayList<Customer> customers = c.customers();
        assertEquals("Rijad", customers.get(0).getFirstName());
        assertEquals("Paketi prometa interneta", customers.get(0).getService().getName());
        assertEquals("Neo", customers.get(0).getService().getaPackage().getName());
    }

    @Test
    void getArchivedContracts() {
        CustomerDAO c = new CustomerDAO();
        ArrayList<Customer> customers = c.customers();
        ArrayList<Contract> result = c.getArchivedContracts(customers.get(0));
        assertEquals(0, result.size());
    }

    @Test
    void addCustomer() {
        CustomerDAO c = new CustomerDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Service s = new Service(1, "Paketi prometa razgovora", new Package(5, "Mobilna telefonija"));
        Customer customer = new Customer(2, "Arman", "Radonja", "arman.radonja@gmail.com", "Sarajevo", "555555", LocalDate.now(), LocalDate.parse("27/02/2025", formatter), s, null);
        c.addCustomer(customer);
        ArrayList<Customer> customers = c.customers();
        assertEquals(2, customers.size());
    }

    @Test
    void editCustomer() {
        CustomerDAO c = new CustomerDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Service s = new Service(1, "Paketi prometa razgovora", new Package(5, "Mobilna telefonija"));
        Customer customer = new Customer(1, "Arman", "Radonja", "arman.radonja@gmail.com", "Sarajevo", "555555", LocalDate.now(), LocalDate.parse("27/02/2025", formatter), s, null);
        c.editCustomer(customer);
        ArrayList<Customer> customers = c.customers();
        assertEquals("Arman", customers.get(0).getFirstName());
    }

    @Test
    void deleteCustomer() {
        CustomerDAO c = new CustomerDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Service s = new Service(1, "Paketi prometa razgovora", new Package(5, "Mobilna telefonija"));
        Customer customer = new Customer(2, "Arman", "Radonja", "arman.radonja@gmail.com", "Sarajevo", "555555", LocalDate.now(), LocalDate.parse("27/02/2025", formatter), s, null);
        c.addCustomer(customer);
        ArrayList<Customer> customers = c.customers();
        assertEquals(2, customers.size());
        c.deleteCustomer(customer);
        customers = c.customers();
        assertEquals(1, customers.size());
    }

}
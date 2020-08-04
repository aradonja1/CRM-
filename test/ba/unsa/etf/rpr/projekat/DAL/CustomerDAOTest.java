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
        assertEquals("Neo", customers.get(0).getService().getListPackages().get(0).getName());
    }

    @Test
    void getArchivedContracts() {
        CustomerDAO c = new CustomerDAO();
        ArrayList<Customer> customers = c.customers();
        ArrayList<Contract> result = c.getArchivedContracts(customers.get(0));
        assertEquals(1, result.size());
    }

    @Test
    void addCustomer() {
        CustomerDAO c = new CustomerDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Package p = new Package(5, "Mobilna telefonija");
        ArrayList<Package> listPackages = new ArrayList<>();
        listPackages.add(p);
        Service s = new Service(1, "Paketi prometa razgovora", listPackages);
        Customer customer = new Customer(2, "Arman", "Radonja", "arman.radonja@gmail.com", "Sarajevo", "555555", LocalDate.now(), LocalDate.parse("27/02/2025", formatter), s, null);
        c.addCustomer(customer);
        ArrayList<Customer> customers = c.customers();
        assertEquals(2, customers.size());
    }

    @Test
    void editCustomer() {
        CustomerDAO c = new CustomerDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Package p = new Package(5, "Mobilna telefonija");
        ArrayList<Package> listPackages = new ArrayList<>();
        listPackages.add(p);
        Service s = new Service(1, "Paketi prometa razgovora", listPackages);
        Customer customer = new Customer(1, "Arman", "Radonja", "arman.radonja@gmail.com", "Sarajevo", "555555", LocalDate.now(), LocalDate.parse("27/02/2025", formatter), s, null);
        c.editCustomer(customer);
        ArrayList<Contract> contracts = c.getContractsFromCustomer(customer);
        ArrayList<Contract> archivedContracts = c.getArchivedContracts(customer);
        ArrayList<Customer> customers = c.customers();
        assertEquals(1, customers.size());
        assertEquals(3, contracts.size());
        assertEquals(2, archivedContracts.size());
        assertEquals("Arman", customers.get(0).getFirstName());
        customer.setFirstName("Rijad");
        c.editCustomer(customer);
        contracts = c.getContractsFromCustomer(customer);
        archivedContracts = c.getArchivedContracts(customer);
        int n = 2;
    }

    @Test
    void deleteCustomer() {
        CustomerDAO c = new CustomerDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Package p = new Package(5, "Mobilna telefonija");
        ArrayList<Package> listPackages = new ArrayList<>();
        listPackages.add(p);
        Service s = new Service(1, "Paketi prometa razgovora", listPackages);
        Customer customer = new Customer(2, "Arman", "Radonja", "arman.radonja@gmail.com", "Sarajevo", "555555", LocalDate.now(), LocalDate.parse("27/02/2025", formatter), s, null);
        c.addCustomer(customer);
        ArrayList<Customer> customers = c.customers();
        assertEquals(2, customers.size());
        c.deleteCustomer(customer);
        customers = c.customers();
        assertEquals(1, customers.size());
    }

    @Test
    void services() {
        CustomerDAO c = new CustomerDAO();
        ArrayList<Service> listServices = c.services();
        assertEquals(3, listServices.size());
        assertEquals("Paketi prometa razgovora", listServices.get(0).getName());
        assertEquals("Paketi prometa interneta", listServices.get(1).getName());
        assertEquals("Prodaja uređaja", listServices.get(2).getName());

    }

    @Test
    void packages() {
        CustomerDAO c = new CustomerDAO();
        ArrayList<Package> listPackages = new ArrayList<>();
        ArrayList<Service> listServices = c.services();
        listPackages = c.packages(listServices.get(0));
        assertEquals(2, listPackages.size());
        listPackages = c.packages(listServices.get(1));
        assertEquals(4, listPackages.size());
        listPackages = c.packages(listServices.get(2));
        assertEquals(2, listPackages.size());
    }

    @Test
    void oneMoreMonthContract() {
        CustomerDAO c = new CustomerDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Package p = new Package(5, "Mobilna telefonija");
        ArrayList<Package> listPackages = new ArrayList<>();
        listPackages.add(p);
        Service s = new Service(1, "Paketi prometa razgovora", listPackages);
        Customer customer = new Customer(2, "Arman", "Radonja", "arman.radonja@gmail.com", "Sarajevo", "555555", LocalDate.now(), LocalDate.parse("03/09/2020", formatter), s, null);
        c.addCustomer(customer);
        ArrayList<Customer> customers = c.customers();
        ArrayList<Customer> result = c.oneMoreMonthContract();
        assertEquals(1, result.size());
    }

    @Test
    void twoMoreMonthContract() {
        CustomerDAO c = new CustomerDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Package p = new Package(5, "Mobilna telefonija");
        ArrayList<Package> listPackages = new ArrayList<>();
        listPackages.add(p);
        Service s = new Service(1, "Paketi prometa razgovora", listPackages);
        Customer customer = new Customer(2, "Arman", "Radonja", "arman.radonja@gmail.com", "Sarajevo", "555555", LocalDate.now(), LocalDate.parse("03/10/2020", formatter), s, null);
        c.addCustomer(customer);
        ArrayList<Customer> customers = c.customers();
        ArrayList<Customer> result = c.twoMoreMonthContract();
        assertEquals(1, result.size());
    }

    @Test
    void threeMoreMonthContract() {
        CustomerDAO c = new CustomerDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Package p = new Package(5, "Mobilna telefonija");
        ArrayList<Package> listPackages = new ArrayList<>();
        listPackages.add(p);
        Service s = new Service(1, "Paketi prometa razgovora", listPackages);
        Customer customer = new Customer(2, "Arman", "Radonja", "arman.radonja@gmail.com", "Sarajevo", "555555", LocalDate.now(), LocalDate.parse("03/11/2020", formatter), s, null);
        c.addCustomer(customer);
        ArrayList<Customer> customers = c.customers();
        ArrayList<Customer> result = c.threeMoreMonthContract();
        assertEquals(1, result.size());
    }

    @Test
    void contracts() {
        CustomerDAO c = new CustomerDAO();
        ArrayList<Contract> contracts = c.contracts();
        assertEquals(2, contracts.size());
    }

    @Test
    void getAllArchivedContracts() {
        CustomerDAO c = new CustomerDAO();
        ArrayList<Contract> result = c.getAllArchivedContracts();
        assertEquals(1, result.size());
    }
}
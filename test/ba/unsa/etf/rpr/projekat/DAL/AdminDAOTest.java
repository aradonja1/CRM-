package ba.unsa.etf.rpr.projekat.DAL;

import ba.unsa.etf.rpr.projekat.Admin;
import ba.unsa.etf.rpr.projekat.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AdminDAOTest {

    @BeforeEach
    void regenerisiBazu() {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();
    }

    @Test
    void addEmployee() {
        AdminDAO a = new AdminDAO();
        Employee e = new Employee(5, "Ermin", "Omeragić", "eomeragic2", "erminBosna");
        a.addEmployee(e);
        ArrayList<Employee> listEmployees = a.employees();
        assertEquals(2, listEmployees.size());
        assertEquals("Ermin", listEmployees.get(1).getFirstName());
        assertEquals("Omeragić", listEmployees.get(1).getLastName());
        assertEquals(2, listEmployees.get(1).getId());
    }

    @Test
    void editEmployee() {
        AdminDAO a = new AdminDAO();
        Employee e = new Employee(2, "Ermin", "Omeragić", "eomeragic2", "erminBosna");
        a.addEmployee(e);
        ArrayList<Employee> listEmployees = a.employees();
        assertEquals(2, listEmployees.size());
        assertEquals("Ermin", listEmployees.get(1).getFirstName());
        assertEquals("Omeragić", listEmployees.get(1).getLastName());
        e.setFirstName("nije više Ermin");
        a.editEmployee(e);
        listEmployees = a.employees();
        assertEquals("nije više Ermin", listEmployees.get(1).getFirstName());
    }

    @Test
    void deleteEmployee() {
        AdminDAO a = new AdminDAO();
        Employee e = new Employee(2, "Ermin", "Omeragić", "eomeragic2", "erminBosna");
        a.addEmployee(e);
        ArrayList<Employee> listEmployees = a.employees();
        assertEquals(2, listEmployees.size());
        a.deleteEmployee(e);
        listEmployees = a.employees();
        assertEquals(1, listEmployees.size());
    }

    @Test
    void employees() {
        AdminDAO a = new AdminDAO();
        ArrayList<Employee> listEmployees = a.employees();
        assertEquals("Senid", listEmployees.get(0).getFirstName());
        assertEquals("Hodžić", listEmployees.get(0).getLastName());
    }

    @Test
    void admins() {
        AdminDAO a = new AdminDAO();
        ArrayList<Admin>  listAdmins = a.admins();
        assertEquals(listAdmins.get(0).getFirstName(), "Adnan");
    }

    @Test
    void createXmlFile() {
        AdminDAO a = new AdminDAO();
        a.addEmployee(new Employee(1, "Arman", "Radonja", "arman", "123"));
        a.createXmlFile();
    }
}
package ba.unsa.etf.rpr.projekat.DAL;

import ba.unsa.etf.rpr.projekat.Admin;
import ba.unsa.etf.rpr.projekat.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOTest {

    @BeforeEach
    void regenerisiBazu() {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();
    }

    @Test
    void addEmployee() {
        EmployeeDAO a = new EmployeeDAO();
        Employee e = new Employee(5, "Ermin", "Omeragić", "eomeragic2", "erminBosna");
        a.addEmployee(e);
        ArrayList<Employee> listEmployees = a.employees();
        assertEquals(3, listEmployees.size());
        assertEquals("Ermin", listEmployees.get(2).getFirstName());
        assertEquals("Omeragić", listEmployees.get(2).getLastName());
    }

    @Test
    void editEmployee() {
        EmployeeDAO a = new EmployeeDAO();
        Employee e = new Employee(2, "Ermin", "Omeragić", "eomeragic2", "erminBosna");
        a.addEmployee(e);
        ArrayList<Employee> listEmployees = a.employees();
        assertEquals(3, listEmployees.size());
        assertEquals("Ermin", listEmployees.get(2).getFirstName());
        assertEquals("Omeragić", listEmployees.get(2).getLastName());
        e.setFirstName("nije više Ermin");
        a.editEmployee(e);
        listEmployees = a.employees();
        assertEquals("nije više Ermin", listEmployees.get(2).getFirstName());
    }

    @Test
    void deleteEmployee() {
        EmployeeDAO a = new EmployeeDAO();
        Employee e = new Employee(2, "Ermin", "Omeragić", "eomeragic2", "erminBosna");
        a.addEmployee(e);
        ArrayList<Employee> listEmployees = a.employees();
        assertEquals(3, listEmployees.size());
        a.deleteEmployee(e);
        listEmployees = a.employees();
        assertEquals(2, listEmployees.size());
    }

    @Test
    void employees() {
        EmployeeDAO a = new EmployeeDAO();
        ArrayList<Employee> listEmployees = a.employees();
        assertEquals("Senid", listEmployees.get(0).getFirstName());
        assertEquals("Hodžić", listEmployees.get(0).getLastName());
    }

    @Test
    void admins() {
        EmployeeDAO a = new EmployeeDAO();
        ArrayList<Admin>  listAdmins = a.admins();
        assertEquals(listAdmins.get(0).getFirstName(), "Adnan");
    }

    @Test
    void readXmlFile() {
        EmployeeDAO a = new EmployeeDAO();
        a.createAndWriteXmlFile();
        ArrayList<Employee> result = a.readXmlFile();
        assertEquals(2, result.size());
    }
}
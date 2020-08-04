package ba.unsa.etf.rpr.projekat.DAL;

import ba.unsa.etf.rpr.projekat.Package;
import ba.unsa.etf.rpr.projekat.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ServiceDAOTest {

    @BeforeEach
    void regenerisiBazu() {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();
    }

    @Test
    void services() {
        ServiceDAO serviceDAO = new ServiceDAO();
        ArrayList<Service> result = serviceDAO.services();
        assertEquals(3, result.size());
    }

    @Test
    void addService() {
        ServiceDAO serviceDAO = new ServiceDAO();
        ArrayList<Service> services = serviceDAO.services();
        Service s = new Service(1, "Nova usluga", null);
        serviceDAO.addService(s);
        services = serviceDAO.services();
        assertEquals(4, services.size());
        assertEquals("Nova usluga", services.get(3).getName());
    }

    @Test
    void editService() {
        ServiceDAO serviceDAO = new ServiceDAO();
        ArrayList<Service> services = serviceDAO.services();
        Service s = new Service(1, "Nova usluga", null);
        serviceDAO.addService(s);
        services = serviceDAO.services();
        assertEquals(4, services.size());
        assertEquals("Nova usluga", services.get(3).getName());
        s.setName("Izmjenjena usluga");
        serviceDAO.editService(s);
        services = serviceDAO.services();
        assertEquals("Izmjenjena usluga", services.get(3).getName());
    }

    @Test
    void getPackagesForService() {
        ServiceDAO serviceDAO = new ServiceDAO();
        ArrayList<Service> result = serviceDAO.services();
        assertEquals(3, result.size());
        ArrayList<Package> listPackages = serviceDAO.getPackagesForService(result.get(2));
        assertEquals(2, listPackages.size());
        assertEquals("LG K61", listPackages.get(0).getName());
    }
}
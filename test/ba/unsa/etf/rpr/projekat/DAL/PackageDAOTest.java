package ba.unsa.etf.rpr.projekat.DAL;

import ba.unsa.etf.rpr.projekat.Package;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PackageDAOTest {

    @BeforeEach
    void regenerisiBazu() {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();
    }


    @Test
    void packages() {
        PackageDAO packageDAO = new PackageDAO();
        ArrayList<Package> result = packageDAO.packages();
        assertEquals(8, result.size());
    }

    @Test
    void addPackage() {
        PackageDAO packageDAO = new PackageDAO();
        Package p = new Package(1, "Huawei P30");
        packageDAO.addPackage(p);
        ArrayList<Package> packages = packageDAO.packages();

        assertEquals(9, packages.size());
    }

    @Test
    void editPackage() {
        PackageDAO packageDAO = new PackageDAO();
        Package p = new Package(1, "Huawei P30");
        packageDAO.addPackage(p);
        ArrayList<Package> packages = packageDAO.packages();
        assertEquals(9, packages.size());

        p.setName("Novi Huawei");
        packageDAO.editPackage(p);
        packages = packageDAO.packages();
        assertEquals("Novi Huawei", packages.get(8).getName());
    }
}
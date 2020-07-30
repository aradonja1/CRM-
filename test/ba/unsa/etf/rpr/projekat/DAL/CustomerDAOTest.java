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

/*
    void regenerateFile() throws SQLException {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstance();
        ArrayList<Grad> gradovi = dao.gradovi();
        assertEquals("London", gradovi.get(0).getNaziv());
        assertEquals("Francuska", gradovi.get(1).getDrzava().getNaziv());
    }

    @Test
    void glavniGrad() throws SQLException {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstance();
        Grad nepoznat = dao.glavniGrad("Bosna i Hercegovina");
        assertNull(nepoznat);
        Grad bech = dao.glavniGrad("Austrija");
        assertEquals("Beč", bech.getNaziv());
    }

    @Test
    void obrisiDrzavu() throws SQLException {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstance();
        // Nepostojeća država, neće se desiti ništa
        dao.obrisiDrzavu("Kina");
        ArrayList<Grad> gradovi = dao.gradovi();
        assertEquals("Pariz", gradovi.get(1).getNaziv());
        assertEquals("Austrija", gradovi.get(2).getDrzava().getNaziv());
    }

    @Test
    void obrisiDrzavu2() throws SQLException {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstance();

        // Nema gradova Beč i Graz koji su iz austrije
        dao.obrisiDrzavu("Austrija");

        ArrayList<Grad> gradovi = dao.gradovi();
        assertEquals(3, gradovi.size());
        assertEquals("London", gradovi.get(0).getNaziv());
        assertEquals("Pariz", gradovi.get(1).getNaziv());
        assertEquals("Manchester", gradovi.get(2).getNaziv());
    }

    @Test
    void dodajGrad() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstance();
        Drzava francuska = dao.nadjiDrzavu("Francuska");
        Grad grad = new Grad();
        grad.setNaziv("Marseille");
        grad.setBrojStanovnika(869815);
        grad.setDrzava(francuska);
        dao.dodajGrad(grad);

        // Marsej je veći od Manchestera i Graza, ali manji od Pariza, Londona i Beča
        ArrayList<Grad> gradovi = dao.gradovi();
        assertEquals("Marseille", gradovi.get(3).getNaziv());
    }

    @Test
    void dodajDrzavu() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        Grad sarajevo = new Grad();
        sarajevo.setNaziv("Sarajevo");
        sarajevo.setBrojStanovnika(500000);
        Drzava bih = new Drzava();
        bih.setNaziv("Bosna i Hercegovina");
        bih.setGlavniGrad(sarajevo);
        sarajevo.setDrzava(bih);

        GeografijaDAO dao = GeografijaDAO.getInstance();
        dao.dodajDrzavu(bih);
        dao.dodajGrad(sarajevo);

        // Provjera
        Grad proba = dao.glavniGrad("Bosna i Hercegovina");
        assertEquals("Sarajevo", proba.getNaziv());
        assertEquals(500000, proba.getBrojStanovnika());
        assertEquals("Bosna i Hercegovina", proba.getDrzava().getNaziv());
    }



    @Test
    void izmijeniGrad() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstance();
        Grad bech = dao.glavniGrad("Austrija");
        bech.setNaziv("Vienna");
        dao.izmijeniGrad(bech);

        ArrayList<Grad> gradovi = dao.gradovi();
        assertEquals("Vienna", gradovi.get(2).getNaziv());
    }

*/
}
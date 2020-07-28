package ba.unsa.etf.rpr.projekat.DAL;

import ba.unsa.etf.rpr.projekat.Customer;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CustomerDAO {

    private Connection conn;

    public ObservableList<Customer> customers = FXCollections.observableArrayList();
    public SimpleObjectProperty<Customer> currentCustomer = new SimpleObjectProperty<>();

    private PreparedStatement allCustomers;

    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ObservableList<Customer> customers) {
        this.customers = customers;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer.get();
    }

    public SimpleObjectProperty<Customer> currentCustomerProperty() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer.set(currentCustomer);
    }

    public CustomerDAO() {
        conn = DatabaseConnection.getInstance().getConn();
        try {
             allCustomers = conn.prepareStatement("SELECT * from customers");
        } catch (SQLException e) {
            e.printStackTrace();
            // regenerateBase();
        }
        try {
            allCustomers = conn.prepareStatement("SELECT * from customers");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Customer> customers() {
        ArrayList<Customer> result = new ArrayList<>();
        try {
            ResultSet rs = allCustomers.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String ime = rs.getString(2);
                String prezime = rs.getString(3);
                String email = rs.getString(4);
                String adress = rs.getString(5);
                String contact = String.valueOf(rs.getInt(6));
                int f = 7;
                result.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), String.valueOf(rs.getInt(6)), null,
                       null, null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }




}

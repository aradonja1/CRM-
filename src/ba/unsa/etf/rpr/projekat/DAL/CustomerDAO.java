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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

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
                result.add(getCustomerFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Customer getCustomerFromResultSet(ResultSet rs) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        try {
            return new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                     rs.getString(5), String.valueOf(rs.getInt(6)), LocalDate.parse(rs.getString(7), formatter),
                     LocalDate.parse(rs.getString(8), formatter), null);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }




}

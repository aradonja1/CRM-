package ba.unsa.etf.rpr.projekat.DAL;

import ba.unsa.etf.rpr.projekat.Contract;
import ba.unsa.etf.rpr.projekat.Customer;
import ba.unsa.etf.rpr.projekat.Package;
import ba.unsa.etf.rpr.projekat.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CustomerDAO {
    private DatabaseConnection db = DatabaseConnection.getInstance();
    private Connection conn;

    private PreparedStatement allCustomersStatement, getConnectionFromId, getServiceFromId, getPackageFromId, getAllContractsFromCustomer;

    public CustomerDAO() {
        conn = db.getConn();
        try {
            allCustomersStatement = conn.prepareStatement("SELECT * FROM customer");
        } catch (SQLException e) {
            db.regenerateBase();
        }
        try {
            allCustomersStatement = conn.prepareStatement("SELECT * FROM customer");
            getConnectionFromId = conn.prepareStatement("SELECT * FROM connection WHERE id=?");
            getServiceFromId = conn.prepareStatement("SELECT * FROM service WHERE id=?");
            getPackageFromId = conn.prepareStatement("SELECT * FROM package WHERE id=?");
            getAllContractsFromCustomer = conn.prepareStatement("SELECT * FROM contract WHERE customer_id=?");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public ArrayList<Customer> customers() {
        ArrayList<Customer> result = new ArrayList<>();
        try {
            ResultSet rs = allCustomersStatement.executeQuery();
            while (rs.next()) {
                Customer c = getCustomerFromResultSet(rs);
                result.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Customer getCustomerFromResultSet(ResultSet rs) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
         try {
             getConnectionFromId.setInt(1,  rs.getInt(9));
             ResultSet rs2 = getConnectionFromId.executeQuery();
             getServiceFromId.setInt(1, rs2.getInt(2));
             getPackageFromId.setInt(1,  rs2.getInt(3));
             ResultSet rs3 = getServiceFromId.executeQuery();
             ResultSet rs4 = getPackageFromId.executeQuery();
             return new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(6), LocalDate.parse(rs.getString(7), formatter),
                    LocalDate.parse(rs.getString(8), formatter), getServiceFromId(rs3.getInt(1), getPackageFromId(rs4.getInt(1))),
                     null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Service getServiceFromId(int serviceId, Package aPackage) {
        try {
            getServiceFromId.setInt(1, serviceId);
            ResultSet rs = getServiceFromId.executeQuery();
            return new Service(rs.getInt(1), rs.getString(2), aPackage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //vraća paket iz baze koji ima id = packageId
    private Package getPackageFromId(int packageId) {
        try {
            getPackageFromId.setInt(1, packageId);
            ResultSet rs = getPackageFromId.executeQuery();
            return new Package(rs.getInt(1), rs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    private Contract getContractsFromCustomer(Customer customer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            getAllContractsFromCustomer.setInt(1, customer.getId());
            ResultSet rs = getAllContractsFromCustomer.executeQuery();
            while (rs.next()) {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DatabaseConnection getDb() {
        return db;
    }
}

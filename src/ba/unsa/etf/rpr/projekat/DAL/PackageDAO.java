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

public class PackageDAO {
    private DatabaseConnection db = DatabaseConnection.getInstance();
    private Connection conn;

    private PreparedStatement addPackageStatement, getIdForNewPackageStatement, allPackagesStatement, editPackageStatement,
                              archivePackageStatement, archiveConnectionsStatement, getCurrentContractStatement, getConnectionFromIdStatement,
                              getServiceFromIdStatement, getPackageFromIdStatement, getPackagesForServiceStatement;

    private CustomerDAO customerDAO = new CustomerDAO();

    public PackageDAO() {
        conn = db.getConn();
        try {
            addPackageStatement = conn.prepareStatement("INSERT INTO package VALUES(?,?,?)");
        } catch (SQLException e) {
            db.regenerateBase();
        }
        try {
            addPackageStatement = conn.prepareStatement("INSERT INTO package VALUES(?,?,?)");
            getIdForNewPackageStatement = conn.prepareStatement("SELECT MAX(id)+1 FROM package");

            allPackagesStatement = conn.prepareStatement("SELECT * FROM package WHERE archived=0");

            editPackageStatement = conn.prepareStatement("UPDATE package SET name=? WHERE id=?");

            archivePackageStatement = conn.prepareStatement("UPDATE package SET archived=1 WHERE id=?");
            archiveConnectionsStatement = conn.prepareStatement("UPDATE connection SET archived=1 WHERE package_id=?");
            getCurrentContractStatement = conn.prepareStatement("SELECT a.id, a.customer_id, a.begin_contract, a.end_contract, a.connection FROM contract a, customer b WHERE b.id=a.customer_id AND b.id=? AND a.begin_contract=b.begin_contract AND a.end_contract=b.end_contract");
            getConnectionFromIdStatement = conn.prepareStatement("SELECT * FROM connection WHERE id=?");
            getServiceFromIdStatement = conn.prepareStatement("SELECT * FROM service WHERE id=?");
            getPackageFromIdStatement = conn.prepareStatement("SELECT * FROM package WHERE id=?");
            getPackagesForServiceStatement = conn.prepareStatement("SELECT package_id FROM connection WHERE service_id=?");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPackage(Package aPackage) {
        try {
            ResultSet rs = getIdForNewPackageStatement.executeQuery();
            aPackage.setId(rs.getInt(1));
            addPackageStatement.setInt(1, aPackage.getId());
            addPackageStatement.setString(2, aPackage.getName());
            addPackageStatement.setInt(3, 0);
            addPackageStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Package> packages() {
        ArrayList<Package> result = new ArrayList<>();
        try {
            ResultSet rs = allPackagesStatement.executeQuery();
            while (rs.next()) {
                result.add(new Package(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void editPackage(Package aPackage) {
        try {
            editPackageStatement.setString(1, aPackage.getName());
            editPackageStatement.setInt(2, aPackage.getId());
            editPackageStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void archivePackage(Package aPackage) {
        try {
            archivePackageStatement.setInt(1, aPackage.getId());
            archiveConnectionsStatement.setInt(1, aPackage.getId());
            archiveConnectionsStatement.execute();
            archivePackageStatement.execute();
            ArrayList<Customer> l = customerDAO.customers();
            for (Customer c : l) {
                if (c.getService().getListPackages().contains(aPackage)) {
                    getCurrentContractStatement.setInt(1, c.getId());
                    ResultSet rs = getCurrentContractStatement.executeQuery();
                    int connId = rs.getInt(5);
                    Contract contract = getContractFromResultSet(rs);
                    customerDAO.concludeContract(contract, connId, c.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Contract getContractFromResultSet(ResultSet rs) {
        try {
            int id = rs.getInt(1);
            int customerId = rs.getInt(2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate beginContract = LocalDate.parse(rs.getString(3), formatter);
            LocalDate endContract = LocalDate.parse(rs.getString(4), formatter);
            int connectionId = rs.getInt(5);
            getConnectionFromIdStatement.setInt(1, connectionId);
            ResultSet rs2 = getConnectionFromIdStatement.executeQuery();
            Service s = getServiceFromConnectionResultSet(rs2);
            return new Contract(id, beginContract, endContract, s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Service getServiceFromConnectionResultSet(ResultSet rs) {
        try {
            getServiceFromIdStatement.setInt(1, rs.getInt(2));
            getPackageFromIdStatement.setInt(1,  rs.getInt(3));
            ResultSet rs3 = getServiceFromIdStatement.executeQuery();
            ResultSet rs4 = getPackageFromIdStatement.executeQuery();
            return getServiceFromId(rs3.getInt(1), getPackageFromId(rs4.getInt(1)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Service getServiceFromId(int serviceId, Package aPackage) {
        try {
            getServiceFromIdStatement.setInt(1, serviceId);
            ResultSet rs = getServiceFromIdStatement.executeQuery();
            getPackagesForServiceStatement.setInt(1, serviceId);
            ResultSet rs2 = getPackagesForServiceStatement.executeQuery();
            ArrayList<Package> listPackages = new ArrayList<>();
            while (rs2.next()) {
                int packageId = rs2.getInt(1);
                getPackageFromIdStatement.setInt(1, packageId);
                ResultSet rs3 = getPackageFromIdStatement.executeQuery();
                if (aPackage.getId() == rs3.getInt(1)) {
                    listPackages.add(new Package(rs3.getInt(1), rs3.getString(2)));
                }
            }
            return new Service(rs.getInt(1), rs.getString(2), listPackages);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Package getPackageFromId(int packageId) {
        try {
            getPackageFromIdStatement.setInt(1, packageId);
            ResultSet rs = getPackageFromIdStatement.executeQuery();
            return new Package(rs.getInt(1), rs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

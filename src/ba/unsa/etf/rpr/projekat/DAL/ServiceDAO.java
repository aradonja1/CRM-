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

public class ServiceDAO {
    private DatabaseConnection db = DatabaseConnection.getInstance();
    private Connection conn;

    private PreparedStatement allServicesStatement, getPackagesForServiceStatement, getPackageFromIdStatement, addServiceStatement, getIdForNewServiceStatement,
                              editServiceStatement, addNewConnectionStatement, getIdForNewConnection, archiveServiceStatement, archiveConnectionStatement,
            getCurrentContractStatement, getConnectionFromIdStatement, getServiceFromIdStatement;

    private PackageDAO packageDAO = new PackageDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    public ServiceDAO() {
        conn = db.getConn();
        try {
            allServicesStatement = conn.prepareStatement("SELECT * FROM service WHERE archived=0");
        } catch (SQLException e) {
            db.regenerateBase();
        }
        try {
            allServicesStatement = conn.prepareStatement("SELECT * FROM service WHERE archived=0");
            getPackagesForServiceStatement = conn.prepareStatement("SELECT package_id FROM connection WHERE service_id=? AND archived=0 ");
            getPackageFromIdStatement = conn.prepareStatement("SELECT * FROM package WHERE id=?  AND archived=0");

            addServiceStatement = conn.prepareStatement("INSERT INTO service VALUES(?,?,?)");
            getIdForNewServiceStatement = conn.prepareStatement("SELECT MAX(id)+1 FROM service");
            getIdForNewConnection = conn.prepareStatement("SELECT MAX(id)+1 FROM connection");
            addNewConnectionStatement = conn.prepareStatement("INSERT INTO connection VALUES(?,?,?,?)");

            editServiceStatement = conn.prepareStatement("UPDATE service SET name=? WHERE id=?");

            archiveServiceStatement = conn.prepareStatement("UPDATE service SET archived=1 WHERE id=?");
            archiveConnectionStatement = conn.prepareStatement("UPDATE connection SET archived=1 WHERE service_id=?");
            getCurrentContractStatement = conn.prepareStatement("SELECT a.id, a.customer_id, a.begin_contract, a.end_contract, a.connection FROM contract a, customer b WHERE b.id=a.customer_id AND b.id=? AND a.begin_contract=b.begin_contract AND a.end_contract=b.end_contract");
            getConnectionFromIdStatement = conn.prepareStatement("SELECT * FROM connection WHERE id=?");
            getServiceFromIdStatement = conn.prepareStatement("SELECT * FROM service WHERE id=?");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Service> services() {
        ArrayList<Service> result = new ArrayList<>();
        try {
            ResultSet rs = allServicesStatement.executeQuery();
            while (rs.next()) {
                Service s = new Service(rs.getInt(1), rs.getString(2), null);
                s.setListPackages(getPackagesForService(s));
                result.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Package> getPackagesForService(Service service) {
        ArrayList<Package> result = new ArrayList<>();
        try {
            getPackagesForServiceStatement.setInt(1, service.getId());
            ResultSet rs = getPackagesForServiceStatement.executeQuery();
            while (rs.next()) {
                getPackageFromIdStatement.setInt(1, rs.getInt(1));
                ResultSet rs2 = getPackageFromIdStatement.executeQuery();
                result.add(new Package(rs2.getInt(1), rs2.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addService(Service service) {
        try {
            ResultSet rs = getIdForNewServiceStatement.executeQuery();
            service.setId(rs.getInt(1));
            addServiceStatement.setInt(1, service.getId());
            addServiceStatement.setString(2, service.getName());
            addServiceStatement.setInt(3, 0);
            addPackageForService(service, service.getListPackages().get(0));
            addServiceStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPackageForService(Service service, Package newPackage) {
        try {
            ResultSet rs = getIdForNewConnection.executeQuery();
            addNewConnectionStatement.setInt(1, rs.getInt(1));
            addNewConnectionStatement.setInt(2, service.getId());
            addNewConnectionStatement.setInt(3, newPackage.getId());
            addNewConnectionStatement.setInt(4,0);
            addNewConnectionStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editService(Service service, boolean addPackage) {
        try {
            editServiceStatement.setString(1, service.getName());
            editServiceStatement.setInt(2, service.getId());
            if (addPackage)
                addPackageForService(service, service.getListPackages().get(service.getListPackages().size() - 1));
            editServiceStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void archiveService(Service service) {
        try {
            //arhiviraj i veze koje imaju u sebi ovaj service
            archiveServiceStatement.setInt(1, service.getId());
            archiveConnectionStatement.setInt(1, service.getId());
            archiveConnectionStatement.execute();
            archiveServiceStatement.execute();

            ArrayList<Customer> l = customerDAO.customers();
            for (Customer c : l) {
                if (c.getService().equals(service)) {
                    getCurrentContractStatement.setInt(1, c.getId());
                    ResultSet rs = getCurrentContractStatement.executeQuery();
                    if (!rs.isClosed()) {
                        int connId = rs.getInt(5);
                        Contract contract = getContractFromResultSet(rs);
                        customerDAO.concludeContract(contract, connId, c.getId());
                    }
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

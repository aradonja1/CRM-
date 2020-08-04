package ba.unsa.etf.rpr.projekat.DAL;

import ba.unsa.etf.rpr.projekat.Package;
import ba.unsa.etf.rpr.projekat.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceDAO {
    private DatabaseConnection db = DatabaseConnection.getInstance();
    private Connection conn;

    private PreparedStatement allServicesStatement, getPackagesForServiceStatement, getPackageFromIdStatement, addServiceStatement, getIdForNewServiceStatement,
                              editServiceStatement, addNewConnectionStatement, getIdForNewConnection;

    public ServiceDAO() {
        conn = db.getConn();
        try {
            allServicesStatement = conn.prepareStatement("SELECT * FROM service");
        } catch (SQLException e) {
            db.regenerateBase();
        }
        try {
            allServicesStatement = conn.prepareStatement("SELECT * FROM service");
            getPackagesForServiceStatement = conn.prepareStatement("SELECT package_id FROM connection WHERE service_id=?");
            getPackageFromIdStatement = conn.prepareStatement("SELECT * FROM package WHERE id=?");

            addServiceStatement = conn.prepareStatement("INSERT INTO service VALUES(?,?)");
            getIdForNewServiceStatement = conn.prepareStatement("SELECT MAX(id)+1 FROM service");
            getIdForNewConnection = conn.prepareStatement("SELECT MAX(id)+1 FROM connection");
            addNewConnectionStatement = conn.prepareStatement("INSERT INTO connection VALUES(?,?,?)");

            editServiceStatement = conn.prepareStatement("UPDATE service SET name=? WHERE id=?");
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



}

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

    private PreparedStatement allServicesStatement, getPackagesForServiceStatement, getPackageFromIdStatement,
                              addServiceStatement, getIdForNewServiceStatement, addPackagesForServiceStatement;

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
            addPackagesForServiceStatement = conn.prepareStatement("INSERT INTO package VALUES(?,?)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addService(Service service) {
        try {
            ResultSet rs = getIdForNewServiceStatement.executeQuery();
            service.setId(rs.getInt(1));
            addServiceStatement.setInt(1, service.getId());
            addServiceStatement.setString(2, service.getName());
            //dodati listu paketa
            //treba malo razmisliti ovdje!
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Service> services() {
        ArrayList<Service> result = new ArrayList<>();
        try {
            ResultSet rs = allServicesStatement.executeQuery();
            while (rs.next()) {
                Service service = new Service(rs.getInt(1), rs.getString(2), null);
                service.setListPackages(getPackagesForService(service));
                result.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<Package> getPackagesForService(Service service) {
        ArrayList<Package> result = new ArrayList<>();
        try {
            getPackagesForServiceStatement.setInt(1, service.getId());
            ResultSet rs = getPackagesForServiceStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                getPackageFromIdStatement.setInt(1, id);
                ResultSet rs2 = getPackageFromIdStatement.executeQuery();
                result.add(new Package(rs2.getInt(1), rs2.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //malo logike ovdje!!!!
        return result;
    }
}

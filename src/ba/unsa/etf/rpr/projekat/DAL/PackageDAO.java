package ba.unsa.etf.rpr.projekat.DAL;

import ba.unsa.etf.rpr.projekat.Package;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PackageDAO {
    private DatabaseConnection db = DatabaseConnection.getInstance();
    private Connection conn;

    private PreparedStatement addPackageStatement, getIdForNewPackageStatement, allPackagesStatement, editPackageStatement;

    public PackageDAO() {
        conn = db.getConn();
        try {
            addPackageStatement = conn.prepareStatement("INSERT INTO package VALUES(?,?)");
        } catch (SQLException e) {
            db.regenerateBase();
        }
        try {
            addPackageStatement = conn.prepareStatement("INSERT INTO package VALUES(?,?)");
            getIdForNewPackageStatement = conn.prepareStatement("SELECT MAX(id)+1 FROM package");

            allPackagesStatement = conn.prepareStatement("SELECT * FROM package");

            editPackageStatement = conn.prepareStatement("UPDATE package SET name=? WHERE id=?");
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

}

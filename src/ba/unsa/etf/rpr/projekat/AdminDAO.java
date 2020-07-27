package ba.unsa.etf.rpr.projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminDAO {
    private static AdminDAO instance;
    private Connection conn;

    public ObservableList<Admin> admins = FXCollections.observableArrayList();

    private PreparedStatement allAdminsStatement;

    public static AdminDAO getInstance() {
        if (instance == null)
            instance = new AdminDAO();
        return instance;
    }

    private AdminDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            allAdminsStatement = conn.prepareStatement("SELECT * FROM admin");
        } catch (SQLException e) {
            regenerateBase();
        }
        try {
            allAdminsStatement = conn.prepareStatement("SELECT * FROM admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void regenerateBase() {
        Scanner ulaz = null;
        try {
            ulaz = new Scanner(new FileInputStream("baza.db.sql"));
            String sqlUpit = "";
            while (ulaz.hasNext()) {
                sqlUpit += ulaz.nextLine();
                if ( sqlUpit.charAt( sqlUpit.length()-1 ) == ';') {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sqlUpit);
                        sqlUpit = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            ulaz.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Admin> admins() {
        ArrayList<Admin> result = new ArrayList<>();
        try {
            ResultSet rs = allAdminsStatement.executeQuery();
            while (rs.next()) {
                LocalDate date = null;
                String d = rs.getString(6);
                result.add(new Admin(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), date, rs.getString(7), rs.getString(8)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


}

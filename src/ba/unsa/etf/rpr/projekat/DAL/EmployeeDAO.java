package ba.unsa.etf.rpr.projekat.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EmployeeDAO {
    private DatabaseConnection db = DatabaseConnection.getInstance();
    private Connection conn;


    public EmployeeDAO() {
        conn = db.getConn();
    }



}

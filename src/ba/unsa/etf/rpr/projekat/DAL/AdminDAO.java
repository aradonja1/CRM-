package ba.unsa.etf.rpr.projekat.DAL;

import ba.unsa.etf.rpr.projekat.Admin;
import ba.unsa.etf.rpr.projekat.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDAO {
    private DatabaseConnection db = DatabaseConnection.getInstance();
    private Connection conn;

    private PreparedStatement addEmployeeStatement, getIdForNewEmployeeStatement, editEmployeeStatement, deleteEmployeeStatement, allEmployeesStatement,
                              allAdminsStatement;

    public AdminDAO() {
        conn = db.getConn();
        try {
            addEmployeeStatement = conn.prepareStatement("INSERT INTO employee VALUES (?,?,?,?,?)");
        } catch (SQLException e) {
            db.regenerateBase();
        }
        try {
            addEmployeeStatement = conn.prepareStatement("INSERT INTO employee VALUES (?,?,?,?,?)");
            getIdForNewEmployeeStatement = conn.prepareStatement("SELECT MAX(id)+1 FROM employee");
            editEmployeeStatement = conn.prepareStatement("UPDATE employee SET first_name=?, last_name=?, username=?, password=? WHERE id=?");
            deleteEmployeeStatement = conn.prepareStatement("DELETE FROM employee WHERE id=?");
            allEmployeesStatement = conn.prepareStatement("SELECT * FROM employee");

            allAdminsStatement = conn.prepareStatement("SELECT * FROM admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEmployee(Employee employee) {
        try {
            ResultSet rs = getIdForNewEmployeeStatement.executeQuery();
            employee.setId(rs.getInt(1));
            addEmployeeStatement.setInt(1, employee.getId());
            addEmployeeStatement.setString(2, employee.getFirstName());
            addEmployeeStatement.setString(3, employee.getLastName());
            addEmployeeStatement.setString(4, employee.getUsername());
            addEmployeeStatement.setString(5, employee.getPassword());
            addEmployeeStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editEmployee(Employee employee) {
        try {
            editEmployeeStatement.setString(1, employee.getFirstName());
            editEmployeeStatement.setString(2, employee.getLastName());
            editEmployeeStatement.setString(3, employee.getUsername());
            editEmployeeStatement.setString(4, employee.getPassword());
            editEmployeeStatement.setInt(5, employee.getId());
            editEmployeeStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(Employee employee) {
        try {
            deleteEmployeeStatement.setInt(1, employee.getId());
            deleteEmployeeStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Employee> employees() {
        ArrayList<Employee> result = new ArrayList<>();
        try {
            ResultSet rs = allEmployeesStatement.executeQuery();
            while (rs.next()) {
                result.add(getEmployeeFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Employee getEmployeeFromResultSet(ResultSet rs) {
        try {
            return new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Admin> admins() {
        ArrayList<Admin> result = new ArrayList<>();
        try {
            ResultSet rs = allAdminsStatement.executeQuery();
            while (rs.next()) {
                result.add(getAdminFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Admin getAdminFromResultSet(ResultSet rs) {
        try {
            return new Admin(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}

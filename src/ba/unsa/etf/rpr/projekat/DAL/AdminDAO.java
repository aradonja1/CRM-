package ba.unsa.etf.rpr.projekat.DAL;

import ba.unsa.etf.rpr.projekat.Admin;
import ba.unsa.etf.rpr.projekat.Employee;
import org.w3c.dom.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


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


    public void createXmlFile() {

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("employees");
            document.appendChild(root);

            for (Employee e : employees()) {
                Element employee = document.createElement("employee");
                root.appendChild(employee);

                Attr attr = document.createAttribute("id");
                attr.setValue(String.valueOf(e.getId()));
                employee.setAttributeNode(attr);

                Element firstName = document.createElement("firstname");
                firstName.appendChild(document.createTextNode(e.getFirstName()));
                employee.appendChild(firstName);

                Element lastname = document.createElement("lastname");
                lastname.appendChild(document.createTextNode(e.getLastName()));
                employee.appendChild(lastname);

                Element username = document.createElement("username");
                username.appendChild(document.createTextNode(e.getUsername()));
                employee.appendChild(username);

                Element password = document.createElement("password");
                password.appendChild(document.createTextNode(e.getPassword()));
                employee.appendChild(password);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("employees.xml"));

            transformer.transform(domSource, streamResult);

            System.out.println("For nicely XML format use this XML Formatter https://www.freeformatter.com/xml-formatter.html#ad-output");
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }

}


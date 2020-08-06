package ba.unsa.etf.rpr.projekat.DAL;

import ba.unsa.etf.rpr.projekat.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReportCustomerDAO {
    private DatabaseConnection db = DatabaseConnection.getInstance();
    private Connection conn;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private CustomerDAO customerDAO = new CustomerDAO();

    private PreparedStatement addCustomerStatement, deleteAllStatement, allCustomersStatement, getNewIdStatement;

    public ReportCustomerDAO() {
        conn = db.getConn();
        try {
            allCustomersStatement = conn.prepareStatement("SELECT * FROM report_customer");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            allCustomersStatement = conn.prepareStatement("SELECT * FROM report_customer");
            addCustomerStatement = conn.prepareStatement("INSERT INTO report_customer VALUES(?,?,?,?,?,?,?,?)");
            getNewIdStatement = conn.prepareStatement("SELECT MAX(id)+1 FROM report_customer");
            deleteAllStatement = conn.prepareStatement("TRUNCATE TABLE report_customer");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addOneOrTwoOrThreeMoreMonthContractCustomers(int number) {
        ArrayList<Customer> listCustomers = new ArrayList<>();
        if (number == 1)
            listCustomers = customerDAO.oneMoreMonthContract(customerDAO.nonarchivedCustomers());
        else if (number == 2)
            listCustomers = customerDAO.twoMoreMonthContract(customerDAO.nonarchivedCustomers());
        else if (number == 3)
            listCustomers = customerDAO.threeMoreMonthContract(customerDAO.nonarchivedCustomers());
        else
            listCustomers = customerDAO.nonarchivedCustomers();
        try {
            deleteAllStatement.execute();
            for (Customer customer : listCustomers) {
                ResultSet rs = getNewIdStatement.executeQuery();
                addCustomerStatement.setInt(1, rs.getInt(1));
                addCustomerStatement.setString(2, customer.getFirstName());
                addCustomerStatement.setString(3, customer.getLastName());
                addCustomerStatement.setString(4, customer.getEmail());
                addCustomerStatement.setString(5, customer.getBeginContract().format(formatter));
                addCustomerStatement.setString(6, customer.getEndContract().format(formatter));
                addCustomerStatement.setString(7, customer.getService().getName());
                addCustomerStatement.setString(8, customer.getService().getListPackages().get(0).getName());
                addCustomerStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

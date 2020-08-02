package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.AdminDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class LoginController {

    public Label dateLabel;
    public Label timeLabel;
    public TextField fldUsername;
    public PasswordField fldPassword;

    private AdminDAO adminDAO = new AdminDAO();

    @FXML
    public void initialize() {
        dateLabel.setText(LocalDate.now().toString());
        timeLabel.setText(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond());
    }

    private boolean loginCorrect(String username, String password) {
        ArrayList<Admin> listAdmins = adminDAO.admins();
        ArrayList<Employee> listEmployees = adminDAO.employees();
        for (Admin a : listAdmins) {
            if (a.getUsername().equals(username) && a.getPassword().equals(password))
                return true;
        }
        for (Employee e : listEmployees) {
            if (e.getUsername().equals(username) && e.getPassword().equals(password))
                return true;
        }
        return false;
    }

    public void onActionLogin(ActionEvent actionEvent) throws IOException {
        ArrayList<Admin> listAdmins = adminDAO.admins();
        ArrayList<Employee> listEmployees = adminDAO.employees();
        String url = new String();
        String title = new String();
        for (Admin a : listAdmins) {
            if (a.getUsername().equals(fldUsername.getText()) && a.getPassword().equals(fldPassword.getText())) {
                url = "/fxml/admin.fxml";
                title = "Admin";
            }
        }
        for (Employee e : listEmployees) {
            if (e.getUsername().equals(fldUsername.getText()) && e.getPassword().equals(fldPassword.getText())) {
                url = "/fxml/customers.fxml";
                title = "CRM";
            }
        }
        if (url.equals("/fxml/admin.fxml") || url.equals("/fxml/customers.fxml")) {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(url));
            stage.setTitle(title);
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.show();
        }
    }

}

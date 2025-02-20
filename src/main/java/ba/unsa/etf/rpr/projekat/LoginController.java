package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.EmployeeDAO;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class LoginController {

    public Label dateLabel;
    public Label timeLabel;
    public TextField fldUsername;
    public PasswordField fldPassword;

    private EmployeeDAO employeeDAO = new EmployeeDAO();

    private ResourceBundle resourceBundle;

    public LoginController(ResourceBundle bundle) {
        resourceBundle = bundle;
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");



    @FXML
    public void initialize() {

        if (resourceBundle.getLocale().getLanguage().equals("eng"))
            dateLabel.setText(LocalDate.now().toString());
        if (resourceBundle.getLocale().getLanguage().equals("bs"))
            dateLabel.setText(LocalDate.now().format(formatter));
//        new Thread(() -> {
//            try {
//                Thread.sleep(200);
//                while (true) {
//                    Platform.runLater(() -> timeLabel.setText(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute()));
//                    Thread.sleep(500);
//                    Platform.runLater(() -> timeLabel.setText(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond()));
//                    Thread.sleep(500);
//
//                }
//            } catch (InterruptedException e) {
//
//            }
//        }).start();
    }

    public void onActionLogin(ActionEvent actionEvent) throws IOException {
        ArrayList<Admin> listAdmins = employeeDAO.admins();
        ArrayList<Employee> listEmployees = employeeDAO.employees();

        for (Admin a : listAdmins) {
            if (a.getUsername().equals(fldUsername.getText()) && a.getPassword().equals(fldPassword.getText())) {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin.fxml"), resourceBundle);
                AdminController ctrl = new AdminController(resourceBundle);
                loader.setController(ctrl);
                Parent root = loader.load();
                stage.setTitle(resourceBundle.getString("adminapp"));
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.setMinHeight(600);
                stage.setMinWidth(1150);
                stage.setResizable(false);
                stage.centerOnScreen();
                stage.show();
                return;
            }
        }
        for (Employee e : listEmployees) {
            if (e.getUsername().equals(fldUsername.getText()) && e.getPassword().equals(fldPassword.getText())) {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customers.fxml"), resourceBundle);
                CustomersController ctrl = new CustomersController(resourceBundle);
                loader.setController(ctrl);
                Parent root = loader.load();
                stage.setTitle(resourceBundle.getString("customersapp"));
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.centerOnScreen();
                stage.show();
                return;
            }
        }

    }

    public void onActionEnglish(ActionEvent actionEvent) {
        Locale.setDefault(new Locale("eng", "ENG"));
        reloadScene();
    }

    public void onActionBosnian(ActionEvent actionEvent) {
        Locale.setDefault(new Locale("bs", "BA"));
        reloadScene();
    }

    private void reloadScene() {
        resourceBundle = ResourceBundle.getBundle("Translation");
        Scene scene = fldUsername.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"), resourceBundle);
        loader.setController(this);
        try {
            scene.setRoot(loader.load());
        } catch (IOException ignored) {

        }
    }

    public void onActionShutDown(ActionEvent actionEvent) {
        System.exit(0);
    }

}
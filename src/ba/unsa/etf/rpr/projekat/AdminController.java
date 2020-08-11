package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.AdminDAO;
import ba.unsa.etf.rpr.projekat.DAL.CustomerDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class AdminController {
    public TextField fldName;
    public TextField fldSurname;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public PasswordField fldRptPassword;
    public Button btnOk;
    public Button btnCancel;
    public ListView<Employee> lstView;

    private ObservableList<Employee> listEmployees;
    private AdminDAO adminDAO = new AdminDAO();
    private Employee employee;
    private boolean ok;
    private int addition = 0;

    public AdminController() {
        listEmployees = FXCollections.observableArrayList(adminDAO.employees());
    }

    private CustomersController customersController = new CustomersController();

    @FXML
    public void initialize() {
        lstView.setItems(listEmployees);

        lstView.getSelectionModel().selectedItemProperty().addListener(((observableValue, e, t1) -> {
            employee = t1;
            if (employee != null) {
                fldName.setText(employee.getFirstName());
                fldSurname.setText(employee.getLastName());
                fldUsername.setText(employee.getUsername());
                fldPassword.setText(employee.getPassword());
                fldRptPassword.setText(employee.getPassword());
            }
        }));

        fldName.textProperty().addListener((obs, oldName, newName) -> {
            if (fldName.getText().isEmpty()) {
                fldName.getStyleClass().removeAll("correctField");
                fldName.getStyleClass().add("incorrectField");
                ok = false;
            } else {
                fldName.getStyleClass().removeAll("incorrectField");
                fldName.getStyleClass().add("correctField");
                ok = true;
            }
        });

        fldSurname.textProperty().addListener((obs, oldSurname, newSurname) -> {
            if (fldSurname.getText().isEmpty()) {
                fldSurname.getStyleClass().removeAll("correctField");
                fldSurname.getStyleClass().add("incorrectField");
                ok = false;
            } else {
                fldSurname.getStyleClass().removeAll("incorrectField");
                fldSurname.getStyleClass().add("correctField");
                ok = true;
            }
        });

    }

    public void onActionOk(ActionEvent actionEvent) {
        if (!ok) return;
        if (employee == null) {
            employee = new Employee();
        }
        employee.setFirstName(fldName.getText());
        employee.setLastName(fldSurname.getText());
        employee.setUsername(fldUsername.getText());
        employee.setPassword(fldPassword.getText());
        if (addition == 1) {
            adminDAO.addEmployee(employee);
            addition = 0;
        } else if (addition == 2){
            adminDAO.editEmployee(employee);
            addition = 0;
        }
        listEmployees.setAll(adminDAO.employees());
        lstView.refresh();
    }

    public void onActionCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void onActionAdd(ActionEvent actionEvent) throws IOException {
        fldName.setText("");
        fldSurname.setText("");
        fldUsername.setText("");
        fldPassword.setText("");
        fldRptPassword.setText("");
        employee = null;
        addition = 1;
    }


    public void onActionEdit(ActionEvent actionEvent) {
        addition = 2;
    }

    public void onActionDelete(ActionEvent actionEvent) {
        if (employee == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete employee "+employee.getFirstName() + " " +employee.getLastName());
        alert.setContentText("Are you sure you want to delete employee " +employee.getFirstName() + " " +employee.getLastName()+"?");
        alert.setResizable(false);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            adminDAO.deleteEmployee(employee);
            listEmployees.setAll(adminDAO.employees());
        }
    }

    public void onActionCustomers(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/customers.fxml"));
        stage.setTitle("CRM");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();
    }


    public void onActionAddService(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/service.fxml"));
        stage.setTitle("Services");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void onActionAddPackage(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/package.fxml"));
        stage.setTitle("Packages");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void onActionReadXML(ActionEvent actionEvent) {
        adminDAO.readXmlFile();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reading XML file");
        alert.setHeaderText("Successfully read the XML file: employee.xml");
        alert.showAndWait();
    }

    public void onActionWriteXML(ActionEvent actionEvent) throws MalformedURLException {
        adminDAO.createAndWriteXmlFile();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Writing XML file");
        alert.setHeaderText("Successfully written to the XML file: employee.xml");
        alert.showAndWait();

        Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirmation.setTitle("Writing XML file");
        alertConfirmation.setHeaderText("Do you want to see written XML file in notepad?");
        alertConfirmation.setContentText("Notepad will be displayed for 3 seconds");
        Optional<ButtonType> result = alertConfirmation.showAndWait();
        if (result.get() == ButtonType.OK)
                openNotepad();
    }

    private void openNotepad() {
        try {
            System.out.println("Opening notepad");
            Runtime runTime = Runtime.getRuntime();
            String file = "employees.xml";
            Process process = runTime.exec("notepad " +file);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Closing notepad");
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onActionCustomer(ActionEvent actionEvent) throws IOException {
        customersController.onActionAdd(actionEvent);
    }

    public void onActionEmployee(ActionEvent actionEvent) throws IOException {
        onActionAdd(actionEvent);
    }

    public void onActionClose(ActionEvent actionEvent) {
        Stage stage = (Stage) fldSurname.getScene().getWindow();
        stage.close();
    }

    public void onActionExport(ActionEvent actionEvent) throws MalformedURLException {
        onActionWriteXML(actionEvent);
    }

    public void onActionImport(ActionEvent actionEvent) {
        onActionReadXML(actionEvent);
    }

    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }


    public void onActionEditEmployee(ActionEvent actionEvent) {
        onActionEdit(actionEvent);
    }

    public void onActionDeleteEmployee(ActionEvent actionEvent) {
        onActionDelete(actionEvent);
    }

}

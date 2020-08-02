package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.AdminDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

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


    public AdminController() {
        listEmployees = FXCollections.observableArrayList(adminDAO.employees());
    }

    @FXML
    public void initialize() {
        lstView.setItems(listEmployees);

        lstView.getSelectionModel().selectedItemProperty().addListener(((observableValue, e, t1) -> {
            employee = t1;
            fldName.setText(employee.getFirstName());
            fldSurname.setText(employee.getLastName());
            fldUsername.setText(employee.getUsername());
            fldPassword.setText(employee.getPassword());
            fldRptPassword.setText(employee.getPassword());
        }));




    }

    public void onActionOk(ActionEvent actionEvent) {

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
    }


    public void onActionEdit(ActionEvent actionEvent) {

    }

    public void onActionDelete(ActionEvent actionEvent) {

    }

    public void onActionCustomers(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/customers.fxml"));
        stage.setTitle("CRM");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();
    }


}

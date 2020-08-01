package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CustomerFormController {

    public TextField fldName;
    public TextField fldSurname;
    public TextField fldAdress;
    public TextField fldContact;
    public DatePicker dpBeginContract;
    public DatePicker dpEndContract;
    public ChoiceBox<Service> cbService;
    public TextField fldEmail;
    public ChoiceBox<Package> cbPackage;
    public Button btnOk;
    public Button btnCancel;

    private boolean ok;
    private Customer customer;
    private ObservableList<Service> listServices = FXCollections.observableArrayList();
    private ObservableList<Package> listPackages = FXCollections.observableArrayList();
    private CustomerDAO customerDAO = new CustomerDAO();

    @FXML
    public void initialize() {
        fldName.textProperty().addListener((obs, oldName, newName) -> {
            if (fldName.getText().isEmpty() || !isAlpha(newName)) {
                fldName.getStyleClass().removeAll("correctField");
                fldName.getStyleClass().add("incorrectField");
                ok = false;
            } else {
                fldName.getStyleClass().removeAll("incorrectField");
                fldName.getStyleClass().add("correctField");
                ok = true;
            }
        });

        fldSurname.textProperty().addListener((obs, oldName, newName) -> {
            if (fldSurname.getText().isEmpty() || !isAlpha(newName)) {
                fldSurname.getStyleClass().removeAll("correctField");
                fldSurname.getStyleClass().add("incorrectField");
                ok = false;
            } else {
                fldSurname.getStyleClass().removeAll("incorrectField");
                fldSurname.getStyleClass().add("correctField");
                ok = true;
            }
        });


        cbService.setItems(FXCollections.observableArrayList(customerDAO.services()));
        if (cbService.getValue() == null) {
            cbService.getSelectionModel().selectFirst();
            cbPackage.setItems(FXCollections.observableArrayList(customerDAO.packages(cbService.getValue())));
            cbPackage.getSelectionModel().selectFirst();
        }
        cbService.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            cbPackage.setItems(FXCollections.observableArrayList(customerDAO.packages(cbService.getValue())));
            cbPackage.getSelectionModel().selectFirst();
        });
    }

    private boolean isAlpha(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isLetter(string.charAt(i)) && string.charAt(i) != ' ')
                return false;
        }
        return true;
    }

    public void onActionOk(ActionEvent actionEvent) {
        if (!ok) return;
        if (customer == null) {
            customer = new Customer();
        }
        customer.setFirstName(fldName.getText());
        customer.setLastName(fldSurname.getText());
        customer.setAdress(fldAdress.getText());
        customer.setContact(fldContact.getText());
        customer.setEmail(fldEmail.getText());
        customer.setBeginContract(dpBeginContract.getValue());
        customer.setEndContract(dpEndContract.getValue());
        //probajmo ovaj nacin
        customer.setService(cbService.getValue());
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }

    public void onActionCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public Customer getCustomer() {
        return customer;
    }

    public CustomerFormController(Customer customer, ArrayList<Service> services) {
        this.customer = customer;
        listServices = FXCollections.observableArrayList(services);
    }
}

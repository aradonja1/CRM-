package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.CustomerDAO;
import ba.unsa.etf.rpr.projekat.DAL.PackageDAO;
import ba.unsa.etf.rpr.projekat.DAL.ServiceDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
    private ServiceDAO serviceDAO = new ServiceDAO();
    private PackageDAO packageDAO = new PackageDAO();

    private ResourceBundle resourceBundle;

    @FXML
    public void initialize() {
        cbService.setItems(FXCollections.observableArrayList(serviceDAO.services()));

        if (customer != null) {
            fldName.setText(customer.getFirstName());
            fldSurname.setText(customer.getLastName());
            fldAdress.setText(customer.getAdress());
            fldContact.setText(customer.getContact());
            fldEmail.setText(customer.getEmail());
            dpBeginContract.setValue(customer.getBeginContract());
            dpEndContract.setValue(customer.getEndContract());

            for (Service s : listServices)
                if (s.getId() == customer.getService().getId())
                    cbService.getSelectionModel().select(s);

            cbPackage.setItems(FXCollections.observableArrayList(serviceDAO.getPackagesForService(cbService.getValue())));
            for (Package p : listPackages)
                if (p.getId() == customer.getService().getListPackages().get(0).getId())
                    cbPackage.getSelectionModel().select(p);

            ok = true;
        } else {
            fldName.textProperty().addListener((obs, oldName, newName) -> {
                if (fldName.getText().trim().isEmpty() || !isAlpha(fldName.getText())) {
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
                if (fldSurname.getText().trim().isEmpty() || !isAlpha(fldSurname.getText())) {
                    fldSurname.getStyleClass().removeAll("correctField");
                    fldSurname.getStyleClass().add("incorrectField");
                    ok = false;
                } else {
                    fldSurname.getStyleClass().removeAll("incorrectField");
                    fldSurname.getStyleClass().add("correctField");
                    ok = true;
                }
            });

            fldContact.textProperty().addListener((obs, oldContact, newContact) -> {
                if (!isNumber(fldContact.getText()) || fldContact.getText().isEmpty() || fldContact.getText().length() < 6 || fldContact.getText().length() > 20) {
                    fldContact.getStyleClass().removeAll("correctField");
                    fldContact.getStyleClass().add("incorrectField");
                    ok = false;
                } else {
                    fldContact.getStyleClass().removeAll("incorrectField");
                    fldContact.getStyleClass().add("correctField");
                    ok = true;
                }
            });

            fldAdress.textProperty().addListener((obs, oldValue, newValue) -> {
                if (!isAdressOk(fldAdress.getText().trim())) {
                    fldAdress.getStyleClass().removeAll("correctField");
                    fldAdress.getStyleClass().add("incorrectField");
                    ok = false;
                } else {
                    fldAdress.getStyleClass().removeAll("incorrectField");
                    fldAdress.getStyleClass().add("correctField");
                    ok = true;
                }
            });

            fldEmail.textProperty().addListener((obs, oldValue, newValue) -> {
                if (!isEmailCorrect(fldEmail.getText())) {
                    fldEmail.getStyleClass().removeAll("correctField");
                    fldEmail.getStyleClass().add("incorrectField");
                    ok = false;
                } else {
                    fldEmail.getStyleClass().removeAll("incorrectField");
                    fldEmail.getStyleClass().add("correctField");
                    ok = true;
                }
            });

            dpBeginContract.valueProperty().addListener((obs, oldValue, newValue) -> {
                if (dpEndContract.getValue() != null) {
                    if (dpBeginContract.getValue().isAfter(dpEndContract.getValue())) {
                        dpBeginContract.getEditor().getStyleClass().removeAll("correctField");
                        dpBeginContract.getEditor().getStyleClass().add("incorrectField");
                        dpEndContract.getEditor().getStyleClass().removeAll("correctField");
                        dpEndContract.getEditor().getStyleClass().add("incorrectField");
                        ok = false;
                    } else {
                        dpBeginContract.getEditor().getStyleClass().removeAll("incorrectField");
                        dpBeginContract.getEditor().getStyleClass().add("correctField");
                        dpEndContract.getEditor().getStyleClass().removeAll("incorrectField");
                        dpEndContract.getEditor().getStyleClass().add("correctField");
                        ok = true;
                    }
                }
            });

            dpEndContract.valueProperty().addListener((obs, oldValue, newValue) -> {
                if (dpBeginContract.getValue() != null) {
                    if (dpBeginContract.getValue().isAfter(dpEndContract.getValue())) {
                        dpEndContract.getEditor().getStyleClass().removeAll("correctField");
                        dpEndContract.getEditor().getStyleClass().add("incorrectField");
                        dpBeginContract.getEditor().getStyleClass().removeAll("correctField");
                        dpBeginContract.getEditor().getStyleClass().add("incorrectField");
                        ok = false;
                    } else {
                        dpEndContract.getEditor().getStyleClass().removeAll("incorrectField");
                        dpEndContract.getEditor().getStyleClass().add("correctField");
                        dpBeginContract.getEditor().getStyleClass().removeAll("incorrectField");
                        dpBeginContract.getEditor().getStyleClass().add("correctField");
                        ok = true;
                    }
                }
            });


            if (cbService.getValue() == null) {
                cbService.getSelectionModel().selectFirst();
                cbPackage.setItems(FXCollections.observableArrayList(serviceDAO.getPackagesForService(cbService.getValue())));
                cbPackage.getSelectionModel().selectFirst();
            }
        }

        cbService.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            cbPackage.setItems(FXCollections.observableArrayList(serviceDAO.getPackagesForService(cbService.getValue())));
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

    private boolean isNumber(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isDigit(string.charAt(i)) && string.charAt(i) != ' ')
                return false;
        }
        return true;
    }

    private boolean isAdressOk(String string) {
        if (string.length() < 4) return false;
        for (int i = 0; i < string.length(); i++) {
            if (!(Character.isLetter(string.charAt(i)) || string.charAt(i) == ' ' || Character.isDigit(string.charAt(i))))
                return false;
        }
        return true;
    }

    private boolean isEmailCorrect(String s) {
        if (s.length() < 8) return false;
        boolean ok = false;
        boolean okk = false;
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == '.')
                okk = true;
        for (int i = 0; i < s.length(); i++) {
            if (!(Character.isDigit(s.charAt(i)) || Character.isLetter(s.charAt(i)) ||  s.charAt(i) == ' ' || s.charAt(i) != '_'))
                ok = false;
            if (okk && s.charAt(i) == '@')
                ok = true;
        }
        return ok;
    }

    public void onActionOk(ActionEvent actionEvent) {
        if (!ok) return;
        if (customer == null) {
            customer = new Customer();
        }
        if (fldName.getText().equals("") || fldSurname.getText().equals("") || fldEmail.getText().equals("") || fldAdress.getText().equals("") || fldContact.getText().equals("") || dpEndContract.getValue() == null || dpBeginContract.getValue() == null)
            return;

        customer.setFirstName(fldName.getText());
        customer.setLastName(fldSurname.getText());
        customer.setAdress(fldAdress.getText());
        customer.setContact(fldContact.getText());
        customer.setEmail(fldEmail.getText());
        customer.setBeginContract(dpBeginContract.getValue());
        customer.setEndContract(dpEndContract.getValue());
        Service service = cbService.getValue();
        ArrayList<Package> p = new ArrayList<>();
        p.add(cbPackage.getValue());
        service.setListPackages(p);
        customer.setService(service);
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

    public CustomerFormController(Customer customer, ArrayList<Service> services, ResourceBundle resourceBundle) {
        this.customer = customer;
        listServices = FXCollections.observableArrayList(services);
        listPackages = FXCollections.observableArrayList(packageDAO.packages());
        this.resourceBundle = resourceBundle;
    }

    public CustomerFormController(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

}

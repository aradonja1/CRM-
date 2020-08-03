package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.CustomerDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class CustomersController {
    public TableView<Customer> tableView;
    public TableColumn<Customer, String> coloumnName;
    public TableColumn<Customer, String> coloumnSurname;
    public TableColumn<Customer, String> coloumnEmail;
    public TableColumn<Customer, String> coloumnBeginContract;
    public TableColumn<Customer, String> coloumnEndContract;
    public TableColumn<Customer, String > coloumnPackage;
    public TableColumn<Customer, String> coloumnService;
    public Button btnSendEmail;
    public Button btnAllCustomers;
    public Button btnOneMonth;
    public Button btnTwoMonths;
    public Button btnThreeMonths;

    private ObservableList<Customer> listCustomers = FXCollections.observableArrayList();
    private CustomerDAO customerDAO;

    @FXML
    public void initialize() {
        tableView.setItems(listCustomers);
        coloumnName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        coloumnSurname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        coloumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        coloumnBeginContract.setCellValueFactory(tableData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            property.setValue(tableData.getValue().getBeginContract().format(formatter));
            return property;
        });
        coloumnEndContract.setCellValueFactory(tableData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            property.setValue(tableData.getValue().getEndContract().format(formatter));
            return property;
        });
        coloumnService.setCellValueFactory(tableData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(tableData.getValue().getService().getName());
            return property;
        });
        coloumnPackage.setCellValueFactory(tableData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(tableData.getValue().getService().getListPackages().get(0).getName());
            return property;
        });
        btnSendEmail.setVisible(false);
    }

    public CustomersController() {
        customerDAO = new CustomerDAO();
        listCustomers = FXCollections.observableArrayList(customerDAO.customers());
    }

    public void onActionAdd(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customerform.fxml"));
        CustomerFormController ctrl = new CustomerFormController(null, customerDAO.services());
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Add customer form");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();

        stage.setOnHiding( event -> {
            Customer customer = ctrl.getCustomer();
            if (customer != null) {
                customerDAO.addCustomer(customer);
                listCustomers.setAll(customerDAO.customers());
            }
        });
    }

    public void onActionEdit(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customerform.fxml"));

        Customer currentCustomer =  tableView.getSelectionModel().getSelectedItem();
        if (currentCustomer == null) return;

        CustomerFormController ctrl = new CustomerFormController(currentCustomer, customerDAO.services());
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Edit customer form");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();

        stage.setOnHiding(event -> {
            Customer customer = ctrl.getCustomer();
            customer.setId(currentCustomer.getId());
            if (customer != null) {
                customerDAO.editCustomer(customer);
                listCustomers.setAll(customerDAO.customers());
            }
        });

    }

    public void onActionDelete(ActionEvent actionEvent) {
        Customer customer = tableView.getSelectionModel().getSelectedItem();
        if (customer == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete customer "+customer.getFirstName() + " " +customer.getLastName());
        alert.setContentText("Are you sure you want to delete customer " +customer.getFirstName() + " " +customer.getLastName()+"?");
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            customerDAO.deleteCustomer(customer);
            listCustomers.setAll(customerDAO.customers());
        }
    }

    public void onActionContracts(ActionEvent actionEvent) throws IOException {
        Customer customer = tableView.getSelectionModel().getSelectedItem();
        if (customer == null) return;

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/contracts.fxml"));
        ContractController  ctrl = new ContractController(customer);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Contracts");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void onActionAllCustomers(ActionEvent actionEvent) {
        listCustomers = FXCollections.observableArrayList(customerDAO.customers());
        tableView.setItems(listCustomers);
        btnSendEmail.setVisible(false);
    }

    public void onActionOneMonth(ActionEvent actionEvent) {
        listCustomers = FXCollections.observableArrayList(customerDAO.oneMoreMonthContract());
        tableView.setItems(listCustomers);
        btnSendEmail.setVisible(true);
    }

    public void onActionTwoMonths(ActionEvent actionEvent) {
        listCustomers = FXCollections.observableArrayList(customerDAO.twoMoreMonthContract());
        tableView.setItems(listCustomers);
    }

    public void onActionThreeMonths(ActionEvent actionEvent) {
        listCustomers = FXCollections.observableArrayList(customerDAO.threeMoreMonthContract());
        tableView.setItems(listCustomers);
    }

    public void onActionSendEmail(ActionEvent actionEvent) {
    }
}

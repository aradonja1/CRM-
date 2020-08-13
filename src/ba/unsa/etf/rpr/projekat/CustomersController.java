package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class CustomersController {
    public TableView<Customer> tableView;
    public TableColumn<Customer, String> coloumnName;
    public TableColumn<Customer, String> coloumnSurname;
    public TableColumn<Customer, String> coloumnEmail;
    public TableColumn<Customer, String> coloumnBeginContract;
    public TableColumn<Customer, String> coloumnEndContract;
    public TableColumn<Customer, String> coloumnPackage;
    public TableColumn<Customer, String> coloumnService;
    public Button btnSendEmail;
    public Button btnAllCustomers;
    public Button btnOneMonth;
    public Button btnTwoMonths;
    public Button btnThreeMonths;
    public Button btnContracts;
    public TextField fldFilter;

    private ObservableList<Customer> listCustomers = FXCollections.observableArrayList();
    private CustomerDAO customerDAO;
    private ServiceDAO serviceDAO = new ServiceDAO();
    private ReportCustomerDAO reportCustomerDAO = new ReportCustomerDAO();

    private DatabaseConnection db = DatabaseConnection.getInstance();

    private ResourceBundle resourceBundle;

    @FXML
    public void initialize() {
        btnAllCustomers.setTooltip(new Tooltip("All customers in system"));
        btnOneMonth.setTooltip(new Tooltip("Customers whose contract expires in a month"));
        btnTwoMonths.setTooltip(new Tooltip("Customers whose contract expires in two month"));
        btnThreeMonths.setTooltip(new Tooltip("Customers whose contract expires in three month"));
        btnContracts.setTooltip(new Tooltip("Archived contracts for selected customer"));

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

    @FXML
    private void searchRecord(KeyEvent ke) {
        FilteredList<Customer> filteredData = new FilteredList<>(listCustomers, b -> true);
        fldFilter.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String lowerCaseFilter = newValue.toLowerCase();
                if (customer.getFirstName().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (customer.getLastName().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (customer.getService().getName().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (customer.getService().getListPackages().get(0).getName().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else
                    return false;
            });
            SortedList<Customer> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedData);
        });
    }

    public CustomersController(ResourceBundle resourceBundle) {
        customerDAO = new CustomerDAO();
        //      listCustomers = FXCollections.observableArrayList(customerDAO.customers());
        listCustomers = FXCollections.observableArrayList(customerDAO.nonarchivedCustomers());
        this.resourceBundle = resourceBundle;
    }

    public void onActionAdd(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customerform.fxml"), resourceBundle);
        CustomerFormController ctrl = new CustomerFormController(null, serviceDAO.services(), resourceBundle);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle(resourceBundle.getString("addcustomerform"));
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();

        stage.setOnHiding(event -> {
            Customer customer = ctrl.getCustomer();
            if (customer != null) {
                customerDAO.addCustomer(customer);
                //       listCustomers.setAll(customerDAO.customers());
                ArrayList<Customer> kk = customerDAO.nonarchivedCustomers();
                listCustomers.setAll(customerDAO.nonarchivedCustomers());

            }
        });
    }

    public void onActionEdit(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customerform.fxml"), resourceBundle);

        Customer currentCustomer = tableView.getSelectionModel().getSelectedItem();
        if (currentCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Edit customer");
            alert.setHeaderText("Cannot edit unselected customer");
            alert.setContentText("Select the customer you want to edit");
            alert.showAndWait();
            return;
        }

        CustomerFormController ctrl = new CustomerFormController(currentCustomer, serviceDAO.services(), resourceBundle);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle(resourceBundle.getString("editcustomerform"));
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();

        stage.setOnHiding(event -> {
            Customer customer = ctrl.getCustomer();
            customer.setId(currentCustomer.getId());
            if (customer != null) {
                customerDAO.editCustomer(customer);
                //     listCustomers.setAll(customerDAO.customers());
                listCustomers.setAll(customerDAO.nonarchivedCustomers());

            }
        });

    }

    public void onActionDelete(ActionEvent actionEvent) {
        Customer customer = tableView.getSelectionModel().getSelectedItem();
        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete customer");
            alert.setHeaderText("Cannot delete unselected customer");
            alert.setContentText("Select the customer you want to delete");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete customer " + customer.getFirstName() + " " + customer.getLastName());
        alert.setContentText("Are you sure you want to delete customer " + customer.getFirstName() + " " + customer.getLastName() + "?");
        alert.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            customerDAO.deleteCustomer(customer);
            //     listCustomers.setAll(customerDAO.customers());
            listCustomers.setAll(customerDAO.nonarchivedCustomers());
        }
    }

    public void onActionAllCustomers(ActionEvent actionEvent) {
        // listCustomers = FXCollections.observableArrayList(customerDAO.customers());
        listCustomers = FXCollections.observableArrayList(customerDAO.nonarchivedCustomers());
        tableView.setItems(listCustomers);
        btnSendEmail.setVisible(false);
        reportCustomerDAO.addOneOrTwoOrThreeMoreMonthContractCustomers(0);
        try {
            new PrintReport().showReport(db.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
        }
    }

    public void onActionOneMonth(ActionEvent actionEvent) {
        listCustomers = FXCollections.observableArrayList(customerDAO.oneMoreMonthContract(customerDAO.nonarchivedCustomers()));
        tableView.setItems(listCustomers);
        btnSendEmail.setVisible(true);
        reportCustomerDAO.addOneOrTwoOrThreeMoreMonthContractCustomers(1);
        try {
            new PrintReport().showReport(db.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
        }

    }

    public void onActionTwoMonths(ActionEvent actionEvent) {
        listCustomers = FXCollections.observableArrayList(customerDAO.twoMoreMonthContract(customerDAO.nonarchivedCustomers()));
        tableView.setItems(listCustomers);
        btnSendEmail.setVisible(true);
        reportCustomerDAO.addOneOrTwoOrThreeMoreMonthContractCustomers(2);
        try {
            new PrintReport().showReport(db.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
        }

    }

    public void onActionThreeMonths(ActionEvent actionEvent) {
        listCustomers = FXCollections.observableArrayList(customerDAO.threeMoreMonthContract(customerDAO.nonarchivedCustomers()));
        tableView.setItems(listCustomers);
        btnSendEmail.setVisible(true);
        reportCustomerDAO.addOneOrTwoOrThreeMoreMonthContractCustomers(3);
        try {
            new PrintReport().showReport(db.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
        }

    }

    public void onActionSendEmail(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/email.fxml"), resourceBundle);

        if (listCustomers.size() == 0) return;

        //Customer currentCustomer =  tableView.getSelectionModel().getSelectedItem();
        //if (currentCustomer == null) return;

        EmailController ctrl = new EmailController(listCustomers, resourceBundle);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle(resourceBundle.getString("emailapp"));
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void onActionAllContracts(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/contracts.fxml"), resourceBundle);
        ContractsController ctrl = new ContractsController(resourceBundle);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle(resourceBundle.getString("contractsapp"));
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void onActionArchievedContracts(ActionEvent actionEvent) throws IOException {
        Customer customer = tableView.getSelectionModel().getSelectedItem();
        if (customer == null) return;

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/contracts.fxml"), resourceBundle);
        ContractsController ctrl = new ContractsController(customer, resourceBundle);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle(resourceBundle.getString("contractsapp"));
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void onActionPrintCustomers(ActionEvent actionEvent) {
        try {
            new PrintReport().showReport(db.getConn());
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public void onActionNew(ActionEvent actionEvent) throws IOException {
        onActionAdd(actionEvent);
    }

    public void onActionClose(ActionEvent actionEvent) {
        Stage stage = (Stage) btnContracts.getScene().getWindow();
        stage.close();
    }

    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void onActionEditCustomer(ActionEvent actionEvent) throws IOException {
        onActionEdit(actionEvent);
    }


    public void onActionDeleteCustomer(ActionEvent actionEvent) {
        onActionDelete(actionEvent);
    }

    public void onActionViewAllCustomers(ActionEvent actionEvent) {
        reportCustomerDAO.addOneOrTwoOrThreeMoreMonthContractCustomers(0);
        try {
            new PrintReport().showReport(db.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
        }    }


    public void onActionForTheMonth(ActionEvent actionEvent) {
        reportCustomerDAO.addOneOrTwoOrThreeMoreMonthContractCustomers(1);
        try {
            new PrintReport().showReport(db.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
        }
    }


    public void onActionForTwoMonths(ActionEvent actionEvent) {
        reportCustomerDAO.addOneOrTwoOrThreeMoreMonthContractCustomers(2);
        try {
            new PrintReport().showReport(db.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
        }
    }

    public void onActionForThreeMonths(ActionEvent actionEvent) {
        reportCustomerDAO.addOneOrTwoOrThreeMoreMonthContractCustomers(2);
        try {
            new PrintReport().showReport(db.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
        }
    }


    public void onActionAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Author: Adnan Radonja");
        alert.setContentText("Faculty of Electrical Engineering Sarajevo\nCustomer Relationship Managment\nAugust 2020");
        alert.showAndWait();
    }
}


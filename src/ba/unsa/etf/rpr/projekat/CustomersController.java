package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.CustomerDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomersController {
    public TableView tableView;
    public TableColumn<Customer, String> coloumnName;
    public TableColumn<Customer, String> coloumnSurname;
    public TableColumn<Customer, String> coloumnEmail;
    public TableColumn<Customer, String> coloumnBeginContract;
    public TableColumn<Customer, String> coloumnEndContract;

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

    }

    public CustomersController() {
        customerDAO = new CustomerDAO();
        listCustomers = FXCollections.observableArrayList(customerDAO.customers());
    }

}

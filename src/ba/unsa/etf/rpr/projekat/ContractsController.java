package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.CustomerDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ContractsController {


    public TableColumn<Contract, String> coloumnBegin;
    public TableColumn<Contract, String> coloumnEnd;
    public TableColumn<Contract, String> coloumnService;
    public TableColumn<Contract, String> coloumnPackage;
    public TableColumn<Contract, String> coloumnState;
    public TableView<Contract> tblView;

    private ObservableList<Contract> listContracts = FXCollections.observableArrayList();
    private Customer customer;
    private CustomerDAO customerDAO = new CustomerDAO();

    @FXML
    public void initialize() {
        tblView.setItems(listContracts);
        coloumnBegin.setCellValueFactory(tableData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            property.setValue(tableData.getValue().getStartContract().format(formatter));
            return property;
        });
        coloumnEnd.setCellValueFactory(tableData -> {
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
        coloumnState.setCellValueFactory(tableData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue("Active");
            if (customerDAO.getAllArchivedContracts().contains(tableData.getValue()))
                property.setValue("Inactive");
            return property;
        });
    }

    public ContractsController(Customer customer) {
        this.customer = customer;
        listContracts = FXCollections.observableArrayList(customerDAO.getArchivedContracts(customer));
    }

    public ContractsController() {
        listContracts = FXCollections.observableArrayList(customerDAO.contracts());
    }
}

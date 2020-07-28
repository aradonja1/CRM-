package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.CustomerDAO;
import ba.unsa.etf.rpr.projekat.DAL.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class CustomersController {

    public TableView<Customer> tblView;
    public TableColumn<Customer, String> colName;
    public TableColumn<Customer, String> colSurname;


    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    private CustomerDAO dao;


    public CustomersController() {
        dao = new CustomerDAO();
     //Sada se desi neka greska i ne moze se ni kompajlirati
        customers = FXCollections.observableArrayList(dao.customers());
    }


    @FXML
    public void initialize() {
        tblView.setItems(customers);
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));

    }

    public void onActionDetails(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/details.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();
    }

    public void onActionAddEmployee(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/details.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();
    }

}

package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.PackageDAO;
import ba.unsa.etf.rpr.projekat.DAL.ServiceDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ServiceController {
    public TextField fldName;
    public ChoiceBox<Package> cbPackages;
    public ListView<Package> listViewPackages;
    public ListView<Service> lstViewServices;

    private ObservableList<Service> listServices;
    private ObservableList<Package> listPackages;
    private ObservableList<Package> allPackages;
    private ServiceDAO serviceDAO = new ServiceDAO();
    private PackageDAO packageDAO = new PackageDAO();
    private Service service;

    @FXML
    public void initialize() {
        lstViewServices.setItems(listServices);

        lstViewServices.getSelectionModel().selectedItemProperty().addListener(((observableValue, e, t1) -> {
            service = t1;
            if (service != null) {
                fldName.setText(service.getName());
                listPackages = FXCollections.observableArrayList(serviceDAO.getPackagesForService(service));
                listViewPackages.setItems(listPackages);
                cbPackages.setItems(FXCollections.observableArrayList(listPackagesForChoiceBox()));
            } else {

             }
        }));


        fldName.textProperty().addListener((obs, oldName, newName) -> {
            if (add && fldName.getText().isEmpty()) {
                fldName.getStyleClass().removeAll("correctField");
                fldName.getStyleClass().add("incorrectField");
            } else if (add){
                fldName.getStyleClass().removeAll("incorrectField");
                fldName.getStyleClass().add("correctField");
            }
        });
    }

    public void onActionOk(ActionEvent actionEvent) {

    }

    public void onActionCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) fldName.getScene().getWindow();
        stage.close();
    }

    public ServiceController() {
        listServices = FXCollections.observableArrayList(serviceDAO.services());
        allPackages = FXCollections.observableArrayList(packageDAO.packages());
    }

    private ArrayList<Package> listPackagesForChoiceBox() {
        ObservableList<Package> result = FXCollections.observableArrayList(allPackages);
        return result.stream().filter(pack -> {
            return !listPackages.contains(pack);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

}

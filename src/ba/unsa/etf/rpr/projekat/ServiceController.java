package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.PackageDAO;
import ba.unsa.etf.rpr.projekat.DAL.ServiceDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    private boolean ok;

    @FXML
    public void initialize() {
        lstViewServices.setItems(listServices);
        cbPackages.setItems(allPackages);

        lstViewServices.getSelectionModel().selectedItemProperty().addListener(((observableValue, e, t1) -> {
            service = t1;
            if (service != null) {
                fldName.setText(service.getName());
                listPackages = FXCollections.observableArrayList(serviceDAO.getPackagesForService(service));
                listViewPackages.setItems(listPackages);
                cbPackages.setItems(FXCollections.observableArrayList(listPackagesForChoiceBox()));
            }
        }));


        fldName.textProperty().addListener((obs, oldName, newName) -> {
            if (fldName.getText().trim().isEmpty()) {
                fldName.getStyleClass().removeAll("correctField");
                fldName.getStyleClass().add("incorrectField");
                ok = false;
            } else {
                fldName.getStyleClass().removeAll("incorrectField");
                fldName.getStyleClass().add("correctField");
                ok = true;
            }
        });
/*
        cbPackages.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (cbPackages.getSelectionModel().getSelectedItem() == null) {
                cbPackages.getStyleClass().removeAll("correctField");
                cbPackages.getStyleClass().add("incorrectField");
                ok = false;
            } else {
                cbPackages.getStyleClass().removeAll("incorrectField");
                cbPackages.getStyleClass().add("correctField");
                ok = true;
            }
        });
  */  }

    public void onActionOk(ActionEvent actionEvent) {
        if (!ok) return;
        if (service == null) {
            service = new Service();
            service.setName(fldName.getText());
            ArrayList<Package> p = new ArrayList<>();
            if (cbPackages.getValue() != null) {
                p.add(cbPackages.getValue());
                service.setListPackages(p);
                serviceDAO.addService(service);
                listServices.setAll(serviceDAO.services());
                listViewPackages.setItems(FXCollections.observableArrayList(p));
                lstViewServices.refresh();

                Stage stage = (Stage) fldName.getScene().getWindow();
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setHeaderText("Choice box must be selected!");
                alert.setContentText("Please, select a package from choice box");
                alert.showAndWait();
            }
        } else {
            service.setName(fldName.getText());
            ArrayList<Package> p = service.getListPackages();
            if (cbPackages.getValue() != null) {
                p.add(cbPackages.getValue());
                service.setListPackages(p);
                serviceDAO.editService(service, true);
            } else {
                serviceDAO.editService(service, false);
            }
            listServices.setAll(serviceDAO.services());

            lstViewServices.refresh();


            Stage stage = (Stage) fldName.getScene().getWindow();
            stage.close();
        }
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

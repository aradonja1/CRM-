package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.ServiceDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServiceController {
    public TextField fldName;
    public ChoiceBox<Package> cbPackages;
    public ListView<Package> listViewPackages;
    public ListView<Service> lstViewServices;

    private ObservableList<Service> listServices;
    private ObservableList<Package> listPackages;
    private ServiceDAO serviceDAO = new ServiceDAO();
    private Service service;

    @FXML
    public void initialize() {
        lstViewServices.setItems(listServices);

        lstViewServices.getSelectionModel().selectedItemProperty().addListener(((observableValue, e, t1) -> {
            service = t1;
            if (service != null) {
                fldName.setText(service.getName());
            }
        }));



    }

    public void onActionOk(ActionEvent actionEvent) {

    }

    public void onActionCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) fldName.getScene().getWindow();
        stage.close();
    }

    public ServiceController() {
        listServices = FXCollections.observableArrayList(serviceDAO.services());
    }
}

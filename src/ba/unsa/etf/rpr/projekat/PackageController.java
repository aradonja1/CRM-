package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.CustomerDAO;
import ba.unsa.etf.rpr.projekat.DAL.PackageDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PackageController {
    public ListView<Package> fldListView;
    public TextField fldName;

    private Package aPackage;
    private PackageDAO packageDAO = new PackageDAO();
    private ObservableList<Package> listPackages;
    private boolean add;

    @FXML
    public void initialize() {
        fldListView.setItems(listPackages);
        fldListView.getSelectionModel().selectedItemProperty().addListener(((observableValue, e, t1) -> {
            aPackage = t1;
            if (aPackage != null) {
                fldName.setText(aPackage.getName());
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
       // if (!ok) return;
        if (aPackage == null) {
            add = true;
            aPackage = new Package();
            aPackage.setName(fldName.getText());
            packageDAO.addPackage(aPackage);
        } else {
            add = false;
            aPackage.setName(fldName.getText());
            packageDAO.editPackage(aPackage);
        }
        listPackages.setAll(packageDAO.packages());
        fldListView.refresh();
    }

    public void onActionCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) fldName.getScene().getWindow();
        stage.close();
    }

    public PackageController() {
        listPackages = FXCollections.observableArrayList(packageDAO.packages());
    }
}

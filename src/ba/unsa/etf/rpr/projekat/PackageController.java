package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.CustomerDAO;
import ba.unsa.etf.rpr.projekat.DAL.PackageDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.ResourceBundle;

public class PackageController {
    public ListView<Package> fldListView;
    public TextField fldName;

    private Package aPackage;
    private PackageDAO packageDAO = new PackageDAO();
    private ObservableList<Package> listPackages;
    private boolean ok;

    private ResourceBundle resourceBundle;

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
    }

    public void onActionOk(ActionEvent actionEvent) {
        if (!ok) return;
        if (aPackage == null) {
            aPackage = new Package();
            aPackage.setName(fldName.getText());
            packageDAO.addPackage(aPackage);
        } else {
            aPackage.setName(fldName.getText());
            packageDAO.editPackage(aPackage);
        }
        listPackages.setAll(packageDAO.packages());
        fldListView.refresh();

        Stage stage = (Stage) fldName.getScene().getWindow();
        stage.close();
    }

    public void onActionCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) fldName.getScene().getWindow();
        stage.close();
    }

    public PackageController(ResourceBundle resourceBundle) {
        listPackages = FXCollections.observableArrayList(packageDAO.packages());
        this.resourceBundle = resourceBundle;
    }

    public void onActionArchive(ActionEvent actionEvent) {

        //kada se pritisne arhiviraj, paket treba arhivirati
        //mora biti selektovan paket koji zelimo arhivirati
        if (aPackage != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Archive package");
            alert.setHeaderText("Archive package "+aPackage.getName());
            alert.setContentText("Are you sure you want to archive the package " +aPackage.getName()+"?");
            alert.setResizable(true);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                packageDAO.archivePackage(aPackage);
                listPackages.setAll(packageDAO.packages());
                fldName.setText("");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot archive unselected package");
            alert.setContentText("Please select the package you want to archive");
            alert.showAndWait();
            aPackage = null;
        }
    }
}

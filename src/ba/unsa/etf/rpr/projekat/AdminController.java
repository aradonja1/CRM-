package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.EmployeeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class AdminController {
    public TextField fldName;
    public TextField fldSurname;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public PasswordField fldRptPassword;
    public Button btnOk;
    public Button btnCancel;
    public ListView<Employee> lstView;

    private ObservableList<Employee> listEmployees;
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private Employee employee;
    private boolean ok;
    private int addition = 0;

    private ResourceBundle resourceBundle;

    public Button btnAdd;
    public Button editEmployeeBtn;
    public Button btnDeleteEmployee;

    public AdminController(ResourceBundle resourceBundle) {
        listEmployees = FXCollections.observableArrayList(employeeDAO.employees());
        this.resourceBundle = resourceBundle;
    }

    private CustomersController customersController = new CustomersController(resourceBundle);

    @FXML
    public void initialize() {
        if (resourceBundle.getLocale().getLanguage().equals("eng")) {
            btnAdd.setTooltip(new Tooltip("Add employee"));
            editEmployeeBtn.setTooltip(new Tooltip("Edit employee"));
            btnDeleteEmployee.setTooltip(new Tooltip("Delete employee"));
        } else if (resourceBundle.getLocale().getLanguage().equals("bs")) {
            btnAdd.setTooltip(new Tooltip("Dodaj uposlenika"));
            editEmployeeBtn.setTooltip(new Tooltip("Uredi uposlenika"));
            btnDeleteEmployee.setTooltip(new Tooltip("Izbrisi uposlenika"));
        }

        lstView.setItems(listEmployees);

        lstView.getSelectionModel().selectedItemProperty().addListener(((observableValue, e, t1) -> {
            employee = t1;
            if (employee != null) {
                fldName.setText(employee.getFirstName());
                fldSurname.setText(employee.getLastName());
                fldUsername.setText(employee.getUsername());
                fldPassword.setText(employee.getPassword());
                fldRptPassword.setText(employee.getPassword());
            }
        }));

        fldName.textProperty().addListener((obs, oldName, newName) -> {
            if (fldName.getText().trim().isEmpty() || !isAlpha(fldName.getText())) {
                fldName.getStyleClass().removeAll("correctField");
                fldName.getStyleClass().add("incorrectField");
                ok = false;
            } else {
                fldName.getStyleClass().removeAll("incorrectField");
                fldName.getStyleClass().add("correctField");
                ok = true;
            }
        });

        fldSurname.textProperty().addListener((obs, oldSurname, newSurname) -> {
            if (fldSurname.getText().trim().isEmpty() || !isAlpha(fldSurname.getText())) {
                fldSurname.getStyleClass().removeAll("correctField");
                fldSurname.getStyleClass().add("incorrectField");
                ok = false;
            } else {
                fldSurname.getStyleClass().removeAll("incorrectField");
                fldSurname.getStyleClass().add("correctField");
                ok = true;
            }
        });

        fldUsername.textProperty().addListener((obs, oldValue, newValue) -> {
            if (fldUsername.getText().trim().isEmpty() || !isUsernameOk(fldUsername.getText())) {
                fldUsername.getStyleClass().removeAll("correctField");
                fldUsername.getStyleClass().add("incorrectField");
                ok = false;
            } else {
                fldUsername.getStyleClass().removeAll("incorrectField");
                fldUsername.getStyleClass().add("correctField");
                ok = true;
            }
        });

        fldPassword.textProperty().addListener((obs, oldValue, newValue) -> {
            if (fldPassword.getText().isEmpty() || !isPasswordOk(fldPassword.getText()) || !fldPassword.getText().equals(fldRptPassword.getText())) {
                fldPassword.getStyleClass().removeAll("correctField");
                fldPassword.getStyleClass().add("incorrectField");
                fldRptPassword.getStyleClass().removeAll("correctField");
                fldRptPassword.getStyleClass().add("incorrectField");
                ok = false;
            } else {
                fldPassword.getStyleClass().removeAll("incorrectField");
                fldPassword.getStyleClass().add("correctField");
                fldRptPassword.getStyleClass().removeAll("incorrectField");
                fldRptPassword.getStyleClass().add("correctField");
                ok = true;
            }
        });

        fldRptPassword.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!fldRptPassword.getText().equals(fldPassword.getText())) {
                fldRptPassword.getStyleClass().removeAll("correctField");
                fldRptPassword.getStyleClass().add("incorrectField");
                fldPassword.getStyleClass().removeAll("correctField");
                fldPassword.getStyleClass().add("incorrectField");
                ok = false;
            } else {
                fldRptPassword.getStyleClass().removeAll("incorrectField");
                fldRptPassword.getStyleClass().add("correctField");
                fldPassword.getStyleClass().removeAll("incorrectField");
                fldPassword.getStyleClass().add("correctField");
                ok = true;
            }
        });

    }

    private boolean isAlpha(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isLetter(string.charAt(i)) && string.charAt(i) != ' ')
                return false;
        }
        return true;
    }

    private boolean isNumber(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isDigit(string.charAt(i)) && string.charAt(i) != ' ')
                return false;
        }
        return true;
    }

    private boolean isUsernameOk(String string) {
        if (string.length() < 5 || string.length() > 16) return false;
        boolean ok = true;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ' || !(string.charAt(i) == '_' || Character.isDigit(string.charAt(i)) || Character.isLetter(string.charAt(i))))
                ok = false;
        }
        return ok;
    }

    private boolean isPasswordOk(String string) {
        if (string.length() < 5 || string.length() > 16) return false;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ')
                return false;
        }
        return true;
    }

    public void onActionOk(ActionEvent actionEvent) {
        if (!ok) return;
        if (employee == null) {
            employee = new Employee();
        }
        if (fldName.getText().equals("") || fldSurname.getText().equals("") || fldUsername.getText().equals("") || fldPassword.getText().equals("") || fldRptPassword.getText().equals(""))
            return;

        employee.setFirstName(fldName.getText());
        employee.setLastName(fldSurname.getText());
        employee.setUsername(fldUsername.getText());
        employee.setPassword(fldPassword.getText());
        if (addition == 1) {
            employeeDAO.addEmployee(employee);
            addition = 0;
        } else if (addition == 2) {
            employeeDAO.editEmployee(employee);
            addition = 0;
        }
        listEmployees.setAll(employeeDAO.employees());
        lstView.refresh();
    }

    public void onActionCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void onActionAdd(ActionEvent actionEvent) throws IOException {
        fldName.setText("");
        fldSurname.setText("");
        fldUsername.setText("");
        fldPassword.setText("");
        fldRptPassword.setText("");
        employee = null;
        addition = 1;
    }


    public void onActionEdit(ActionEvent actionEvent) {
        addition = 2;
    }

    public void onActionDelete(ActionEvent actionEvent) {
        if (employee == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        if (resourceBundle.getLocale().getLanguage().equals("eng")) {
            alert.setTitle("Delete");
            alert.setHeaderText("Delete employee " + employee.getFirstName() + " " + employee.getLastName());
            alert.setContentText("Are you sure you want to delete employee " + employee.getFirstName() + " " + employee.getLastName() + "?");
        } else if (resourceBundle.getLocale().getLanguage().equals("bs")) {
            alert.setTitle("Brisanje");
            alert.setHeaderText("Obrisi uposlenika " + employee.getFirstName() + " " + employee.getLastName());
            alert.setContentText("Da li ste sigurni da zelite obrisati uposlenika " + employee.getFirstName() + " " + employee.getLastName() + "?");
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("Uredu");
            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
            cancelButton.setText("Odustani");
        }
        alert.setResizable(false);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            employeeDAO.deleteEmployee(employee);
            listEmployees.setAll(employeeDAO.employees());
        }
    }

    public void onActionCustomers(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customers.fxml"), resourceBundle);
        CustomersController ctrl = new CustomersController(resourceBundle);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle(resourceBundle.getString("customersapp"));
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();
    }


    public void onActionAddService(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/service.fxml"), resourceBundle);

        ServiceController ctrl = new ServiceController(resourceBundle);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle(resourceBundle.getString("serviceapp"));
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void onActionAddPackage(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/package.fxml"), resourceBundle);

        PackageController ctrl = new PackageController(resourceBundle);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle(resourceBundle.getString("packageapp"));
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void onActionReadXML(ActionEvent actionEvent) {
        employeeDAO.readXmlFile();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (resourceBundle.getLocale().getLanguage().equals("eng")) {
            alert.setTitle("Reading XML file");
            alert.setHeaderText("Successfully read the XML file: employee.xml");
        } else if (resourceBundle.getLocale().getLanguage().equals("bs")) {
            alert.setTitle("Citanje XML datoteke");
            alert.setHeaderText("Uspjesno procitana XML datoteka: employee.xml");
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("Uredu");
        }
        alert.showAndWait();
    }

    public void onActionWriteXML(ActionEvent actionEvent) throws MalformedURLException {
        employeeDAO.createAndWriteXmlFile();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (resourceBundle.getLocale().getLanguage().equals("eng")) {
            alert.setTitle("Writing XML file");
            alert.setHeaderText("Successfully written to the XML file: employee.xml");
        } else if (resourceBundle.getLocale().getLanguage().equals("bs")) {
            alert.setTitle("Upisivanje XML datoteke");
            alert.setHeaderText("Uspjesno upisana XML datoteka: employee.xml");
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("Uredu");
        }
        alert.showAndWait();

        Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        if (resourceBundle.getLocale().getLanguage().equals("eng")) {
            alertConfirmation.setTitle("Writing XML file");
            alertConfirmation.setHeaderText("Do you want to see written XML file in notepad?");
            alertConfirmation.setContentText("Notepad will be displayed for 3 seconds");
        } else if (resourceBundle.getLocale().getLanguage().equals("bs")) {
            alertConfirmation.setTitle("Upisivanje XML datoteke");
            alertConfirmation.setHeaderText("Da li zelite da vidite upisanu XML datoteku u notepadu?");
            alertConfirmation.setContentText("Notepad ce biti prikazan na 3 sekunde");
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("Uredu");
        }
        Optional<ButtonType> result = alertConfirmation.showAndWait();
        if (result.get() == ButtonType.OK)
            openNotepad();
    }

    private void openNotepad() {
        try {
            System.out.println("Opening notepad");
            Runtime runTime = Runtime.getRuntime();
            String file = "employees.xml";
            Process process = runTime.exec("notepad " + file);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Closing notepad");
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onActionCustomer(ActionEvent actionEvent) throws IOException {
        customersController = new CustomersController(resourceBundle);
        customersController.onActionAdd(actionEvent);
    }

    public void onActionEmployee(ActionEvent actionEvent) throws IOException {
        onActionAdd(actionEvent);
    }

    public void onActionClose(ActionEvent actionEvent) {
        Stage stage = (Stage) fldSurname.getScene().getWindow();
        stage.close();
    }

    public void onActionExport(ActionEvent actionEvent) throws MalformedURLException {
        onActionWriteXML(actionEvent);
    }

    public void onActionImport(ActionEvent actionEvent) {
        onActionReadXML(actionEvent);
    }

    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }


    public void onActionEditEmployee(ActionEvent actionEvent) {
        onActionEdit(actionEvent);
    }

    public void onActionDeleteEmployee(ActionEvent actionEvent) {
        onActionDelete(actionEvent);
    }

    public void onActionViewCustomer(ActionEvent actionEvent) throws IOException {
        onActionCustomers(actionEvent);
    }

    public void onActionViewPackage(ActionEvent actionEvent) throws IOException {
        onActionAddPackage(actionEvent);
    }

    public void onActionViewService(ActionEvent actionEvent) throws IOException {
        onActionAddService(actionEvent);
    }

    public void onActionAbout(ActionEvent actionEvent) {
        customersController = new CustomersController(resourceBundle);
        customersController.onActionAbout(actionEvent);
    }

    public void onActionbtnAdd(ActionEvent actionEvent) throws IOException {
        onActionAdd(actionEvent);
    }

    public void onActionEditEmployee2(ActionEvent actionEvent)  {
        onActionEditEmployee(actionEvent);
    }

    public void onActionDeleteEmployee2(ActionEvent actionEvent)  {
        onActionDeleteEmployee(actionEvent);
    }
}

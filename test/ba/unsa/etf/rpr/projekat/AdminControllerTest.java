package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.EmployeeDAO;
import ba.unsa.etf.rpr.projekat.DAL.CustomerDAO;
import ba.unsa.etf.rpr.projekat.DAL.DatabaseConnection;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class AdminControllerTest {
    Stage theStage;
    EmployeeDAO employeeDAO;
    CustomerDAO customerDAO;

    @Start
    public void start (Stage stage) throws Exception {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();
        employeeDAO = new EmployeeDAO();
        customerDAO = new CustomerDAO();

        Locale.setDefault(new Locale("eng", "ENG"));
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin.fxml"), bundle);
        AdminController ctrl = new AdminController(bundle);
        loader.setController(ctrl);
        Parent root = loader.load();

        stage.setTitle("Admin");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
        stage.toFront();
        theStage = stage;
    }

    @Test
    public void onActionAdd (FxRobot robot) {
        Button btnAdd = robot.lookup("#addEmployeeBtn").queryAs(Button.class);
        assertNotNull(btnAdd);

        ArrayList<Employee> listEmployees = employeeDAO.employees();
        assertEquals(2, listEmployees.size());

        robot.clickOn("#addEmployeeBtn");
        robot.clickOn("#fldName");
        robot.write("Novi");
        robot.clickOn("#fldSurname");
        robot.write("Uposlenik");
        robot.clickOn("#fldUsername");
        robot.write("noviu1");
        robot.clickOn("#fldPassword");
        robot.write("novi123");
        robot.clickOn("#fldRptPassword");
        robot.write("novi123");
        robot.clickOn("#btnOk");

        listEmployees = employeeDAO.employees();
        assertEquals(3, listEmployees.size());

        boolean ok = false;
        for (Employee e : listEmployees) {
            if (e.getFirstName().equals("Novi") && e.getLastName().equals("Uposlenik"))
                ok = true;
        }
        assertTrue(ok);
    }

    @Test
    public void onActionbtnAdd(FxRobot robot) {
        robot.clickOn("#btnAdd");

        ArrayList<Employee> listEmployees = employeeDAO.employees();
        assertEquals(2, listEmployees.size());
        robot.clickOn("#fldName");
        robot.write("Novi");
        robot.clickOn("#fldSurname");
        robot.write("Uposlenik");
        robot.clickOn("#fldUsername");
        robot.write("noviu1");
        robot.clickOn("#fldPassword");
        robot.write("novi123");
        robot.clickOn("#fldRptPassword");
        robot.write("novi123");
        robot.clickOn("#btnOk");

        listEmployees = employeeDAO.employees();
        assertEquals(3, listEmployees.size());

        boolean ok = false;
        for (Employee e : listEmployees) {
            if (e.getFirstName().equals("Novi") && e.getLastName().equals("Uposlenik"))
                ok = true;
        }
        assertTrue(ok);

    }


    @Test
    public void onActionEdit (FxRobot robot) {
        Button btdEdit = robot.lookup("#btnEdit").queryAs(Button.class);
        assertNotNull(btdEdit);

        ArrayList<Employee> listEmployees = employeeDAO.employees();
        assertEquals(2, listEmployees.size());

        robot.clickOn("Senid Hodžić");
        robot.clickOn("#btnEdit");

        robot.clickOn("#fldName");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);

        robot.write("Izmjena");
        robot.clickOn("#fldSurname");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("Uposlenika");
        robot.clickOn("#fldUsername");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("izmjena1");
        robot.clickOn("#fldPassword");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("izmjena123");
        robot.clickOn("#fldRptPassword");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("izmjena123");
        robot.clickOn("#btnOk");

        listEmployees = employeeDAO.employees();
        assertEquals(2, listEmployees.size());

        boolean ok = false;
        for (Employee e : listEmployees) {
            if (e.getFirstName().equals("Izmjena") && e.getLastName().equals("Uposlenika"))
                ok = true;
        }
        assertTrue(ok);
    }

    @Test
    public void onActionEditEmployee2(FxRobot robot) {
        robot.clickOn("Senid Hodžić");

        robot.clickOn("#edit");
        robot.clickOn("#edit2");

        ArrayList<Employee> listEmployees = employeeDAO.employees();
        assertEquals(2, listEmployees.size());

        robot.clickOn("#fldName");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);

        robot.write("Izmjena");
        robot.clickOn("#fldSurname");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("Uposlenika");
        robot.clickOn("#fldUsername");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("izmjena1");
        robot.clickOn("#fldPassword");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("izmjena123");
        robot.clickOn("#fldRptPassword");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("izmjena123");
        robot.clickOn("#btnOk");

        listEmployees = employeeDAO.employees();
        assertEquals(2, listEmployees.size());

        boolean ok = false;
        for (Employee e : listEmployees) {
            if (e.getFirstName().equals("Izmjena") && e.getLastName().equals("Uposlenika"))
                ok = true;
        }
        assertTrue(ok);

    }

    @Test
    public void onActionEditEmployee(FxRobot robot) {
        robot.clickOn("Senid Hodžić");

        robot.clickOn("#editEmployeeBtn");
        onActionEdit(robot);

        ArrayList<Employee> listEmployees = employeeDAO.employees();
        assertEquals(2, listEmployees.size());

        robot.clickOn("#fldName");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);

        robot.write("Izmjena");
        robot.clickOn("#fldSurname");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("Uposlenika");
        robot.clickOn("#fldUsername");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("izmjena1");
        robot.clickOn("#fldPassword");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("izmjena123");
        robot.clickOn("#fldRptPassword");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("izmjena123");
        robot.clickOn("#btnOk");

        listEmployees = employeeDAO.employees();
        assertEquals(2, listEmployees.size());

        boolean ok = false;
        for (Employee e : listEmployees) {
            if (e.getFirstName().equals("Izmjena") && e.getLastName().equals("Uposlenika"))
                ok = true;
        }
        assertTrue(ok);

    }

    @Test
    public void onActionDelete(FxRobot robot) {
        ArrayList<Employee> employees = employeeDAO.employees();
        assertEquals(2, employees.size());

        robot.clickOn("Senid Hodžić");
        robot.clickOn("#btnDelete");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        employees = employeeDAO.employees();
        assertEquals(1, employees.size());
    }

    @Test
    public void onActionDeleteEmployee(FxRobot robot) {
        ArrayList<Employee> employees = employeeDAO.employees();
        assertEquals(2, employees.size());

        robot.clickOn("Senid Hodžić");

        robot.clickOn("#btnDeleteEmployee");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        employees = employeeDAO.employees();
        assertEquals(1, employees.size());

    }

    @Test
    public void onActionDeleteEmployee2(FxRobot robot) {
        ArrayList<Employee> employees = employeeDAO.employees();
        assertEquals(2, employees.size());

        robot.clickOn("Senid Hodžić");

        robot.clickOn("#edit");
        robot.clickOn("#dltCstmr");
        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        employees = employeeDAO.employees();
        assertEquals(1, employees.size());

    }

    @Test
    public void onActionCustomers(FxRobot robot) {
        ArrayList<Customer> customers = customerDAO.nonarchivedCustomers();
        assertEquals(1, customers.size());

        robot.clickOn("#btnCustomers");

        robot.lookup("#fldFilter").tryQuery().isPresent();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> theStage.hide());
        TextField filter = robot.lookup("#fldFilter").queryAs(TextField.class);
        assertNotNull(filter);

        Stage stage = (Stage) filter.getScene().getWindow();
        Platform.runLater(() -> stage.close());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> theStage.show());
    }

    @Test
    public void onActionViewCustomers(FxRobot robot) {
        robot.clickOn("#view");
        robot.clickOn("#cstms");
        ArrayList<Customer> customers = customerDAO.nonarchivedCustomers();
        assertEquals(1, customers.size());


        robot.lookup("#fldFilter").tryQuery().isPresent();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> theStage.hide());
        TextField filter = robot.lookup("#fldFilter").queryAs(TextField.class);
        assertNotNull(filter);

        Stage stage = (Stage) filter.getScene().getWindow();
        Platform.runLater(() -> stage.close());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> theStage.show());
    }

    @Test
    public void onActionAddService(FxRobot robot) {
        ArrayList<Service> services = customerDAO.services();
        assertEquals(3, services.size());

        robot.clickOn("#btnServices");

        robot.lookup("#fldName").tryQuery().isPresent();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> theStage.hide());

        TextField name = robot.lookup("#fldName").queryAs(TextField.class);
        assertNotNull(name);

        robot.clickOn("#btnCancel");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> theStage.show());
    }

    @Test
    public void onActionViewService(FxRobot robot) {
        robot.clickOn("#view");
        robot.clickOn("#srvs");
        ArrayList<Service> services = customerDAO.services();
        assertEquals(3, services.size());


        robot.lookup("#fldName").tryQuery().isPresent();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> theStage.hide());

        TextField name = robot.lookup("#fldName").queryAs(TextField.class);
        assertNotNull(name);

        robot.clickOn("#btnCancel");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> theStage.show());
    }

    @Test
    public void onActionAddPackage(FxRobot robot) {
        ArrayList<Package> packages = customerDAO.packages();
        assertEquals(8, packages.size());

        robot.clickOn("#btnPackages");

        robot.lookup("#fldName").tryQuery().isPresent();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> theStage.hide());

        TextField name = robot.lookup("#fldName").queryAs(TextField.class);
        assertNotNull(name);
        robot.clickOn("#btnCancel");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> theStage.show());
    }

    @Test
    public void onActionAbout(FxRobot robot) {
        robot.clickOn("#help");
        robot.clickOn("#abt");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        assertTrue(true);
    }

    @Test
    public void onActionViewPackage(FxRobot robot) {
        robot.clickOn("#view");
        robot.clickOn("#pcks");
        ArrayList<Package> packages = customerDAO.packages();
        assertEquals(8, packages.size());

        robot.lookup("#fldName").tryQuery().isPresent();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> theStage.hide());

        TextField name = robot.lookup("#fldName").queryAs(TextField.class);
        assertNotNull(name);
        robot.clickOn("#btnCancel");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> theStage.show());
    }

    @Test
    public void onActionWriteXML(FxRobot robot) {
        robot.clickOn("#wXML");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        assertTrue(true);

        DialogPane dialogPane2 = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton2 = (Button) dialogPane2.lookupButton(ButtonType.OK);
        robot.clickOn(okButton2);

        try {
            Thread.sleep(3100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(true);
    }

    @Test
    public void onActionReadXML(FxRobot robot) {
        robot.clickOn("#rXML");
        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        assertTrue(true);

    }
}
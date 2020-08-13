package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.AdminDAO;
import ba.unsa.etf.rpr.projekat.DAL.DatabaseConnection;
import ba.unsa.etf.rpr.projekat.DAL.PackageDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class AdminControllerTest {
    Stage theStage;
    AdminDAO adminDAO;

    @Start
    public void start (Stage stage) throws Exception {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();
        adminDAO = new AdminDAO();

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

        ArrayList<Employee> listEmployees = adminDAO.employees();
        assertEquals(1, listEmployees.size());

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

        listEmployees = adminDAO.employees();
        assertEquals(2, listEmployees.size());

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

        ArrayList<Employee> listEmployees = adminDAO.employees();
        assertEquals(1, listEmployees.size());

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

        listEmployees = adminDAO.employees();
        assertEquals(1, listEmployees.size());

        boolean ok = false;
        for (Employee e : listEmployees) {
            if (e.getFirstName().equals("Izmjena") && e.getLastName().equals("Uposlenika"))
                ok = true;
        }
        assertTrue(ok);
    }

    @Test
    public void onActionDelete(FxRobot robot) {
        ArrayList<Employee> employees = adminDAO.employees();
        assertEquals(1, employees.size());

        robot.clickOn("Senid Hodžić");
        robot.clickOn("#btnDelete");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        employees = adminDAO.employees();
        assertEquals(0, employees.size());
    }


}
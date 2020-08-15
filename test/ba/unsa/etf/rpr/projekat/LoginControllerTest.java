package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.DatabaseConnection;
import ba.unsa.etf.rpr.projekat.DAL.PackageDAO;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class LoginControllerTest {
    Stage theStage;
    PackageDAO packageDAO;

    @Start
    public void start (Stage stage) throws Exception {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();

        Locale.setDefault(new Locale("eng", "ENG"));
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"), bundle);
        LoginController ctrl = new LoginController(bundle);
        loader.setController(ctrl);
        Parent root = loader.load();

        stage.setTitle("Login");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
        stage.toFront();
        theStage = stage;
    }

    @Test
    public void onActionLoginAdmin(FxRobot robot) {
        robot.clickOn("#fldUsername");
        robot.write("admin");

        robot.clickOn("#fldPassword");
        robot.write("admin");

        robot.clickOn("#btnLogin");

        robot.lookup("#fldName").tryQuery().isPresent();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TextField name = robot.lookup("#fldName").queryAs(TextField.class);
        assertNotNull(name);


        Stage stage = (Stage) name.getScene().getWindow();
        Platform.runLater(() -> stage.close());
        assertTrue(true);
    }

    @Test
    public void onActionLoginEmployee(FxRobot robot) {
        robot.clickOn("#fldUsername");
        robot.write("employee");

        robot.clickOn("#fldPassword");
        robot.write("employee");

        robot.clickOn("#btnLogin");

        robot.lookup("#fldFilter").tryQuery().isPresent();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TextField filter = robot.lookup("#fldFilter").queryAs(TextField.class);
        assertNotNull(filter);


        Stage stage = (Stage) filter.getScene().getWindow();
        Platform.runLater(() -> stage.close());
        assertTrue(true);
    }
}
package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.CustomerDAO;
import ba.unsa.etf.rpr.projekat.DAL.DatabaseConnection;
import ba.unsa.etf.rpr.projekat.DAL.EmployeeDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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
class CustomerFormControllerTest {
    Stage theStage;
    EmployeeDAO employeeDAO;
    CustomerDAO customerDAO;

    @Start
    public void start (Stage stage) throws Exception {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();

        customerDAO = new CustomerDAO();

        Locale.setDefault(new Locale("eng", "ENG"));
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customerform.fxml"), bundle);
        CustomerFormController ctrl = new CustomerFormController(bundle);
        loader.setController(ctrl);
        Parent root = loader.load();

        stage.setTitle("Customer form");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
        stage.toFront();
        theStage = stage;
    }

    @Test
    public void onActionOk(FxRobot robot) {
        robot.clickOn("#fldName");
        robot.write("New");
        robot.clickOn("#btnOk");

        assertNotNull(true);

        robot.clickOn("#fldSurname");
        robot.write("Customer");
        robot.clickOn("#btnOk");

        assertNotNull(true);

        robot.clickOn("#fldAdress");
        robot.write("Mostar");

        robot.clickOn("#fldEmail");
        robot.write("novikorisnik@gmail.com");

        TextField email = robot.lookup("#fldEmail").queryAs(TextField.class);
        Background bg = email.getBackground();
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().toString().contains("9acd32"))
                colorFound = true;
        assertTrue(colorFound);

        robot.clickOn("#fldContact");
        robot.write("aaaaaaaaaa");

        TextField contact = robot.lookup("#fldContact").queryAs(TextField.class);
        bg = contact.getBackground();
        colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().toString().contains("ffb6c1"))
                colorFound = true;
        assertTrue(colorFound);

        robot.clickOn("#fldContact");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("061888999");

        colorFound = false;
        bg = contact.getBackground();
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().toString().contains("9acd32"))
                colorFound = true;
        assertTrue(colorFound);


        robot.clickOn("#dpBeginContract");
        robot.write("2020-07-29");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);

        robot.clickOn("#dpEndContract");
        robot.write("2020-09-29");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);

        DatePicker date = robot.lookup("#dpBeginContract").queryAs(DatePicker.class);
        colorFound = false;
        bg = contact.getBackground();
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().toString().contains("9acd32"))
                colorFound = true;
        assertTrue(colorFound);

        DatePicker date2 = robot.lookup("#dpEndContract").queryAs(DatePicker.class);
        colorFound = false;
        bg = contact.getBackground();
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().toString().contains("9acd32"))
                colorFound = true;
        assertTrue(colorFound);

        robot.clickOn("#cbService");
        robot.clickOn("Prodaja uređaja");

        robot.clickOn("#cbPackage");
        robot.clickOn("Samsung Galaxy A41");

        robot.clickOn("#btnOk");

        assertNotNull(true);
    }

}
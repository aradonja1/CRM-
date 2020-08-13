package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.DatabaseConnection;
import ba.unsa.etf.rpr.projekat.DAL.ServiceDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class ServiceControllerTest {
    Stage theStage;
    ServiceDAO serviceDAO;

    @Start
    public void start(Stage stage) throws Exception {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();
        serviceDAO = new ServiceDAO();

        Locale.setDefault(new Locale("eng", "ENG"));
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/service.fxml"), bundle);
        ServiceController ctrl = new ServiceController(bundle);
        loader.setController(ctrl);
        Parent root = loader.load();

        stage.setTitle("Services");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
        stage.toFront();
        theStage = stage;
    }

    @Test
    public void testTablesView(FxRobot robot) {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();

        ListView<Service> listViewServices = robot.lookup("#lstViewServices").queryAs(ListView.class);
        ListView<Package> listViewPackages = robot.lookup("#listViewPackages").queryAs(ListView.class);
        assertEquals(3, listViewServices.getItems().size());
        assertEquals(0, listViewPackages.getItems().size());
    }

    @Test
    public void onActionOk(FxRobot robot) {
        Button btnOk = robot.lookup("#btnOk").queryAs(Button.class);
        assertNotNull(btnOk);

        robot.clickOn("#fldName");
        robot.write("New service");

        ChoiceBox<Package> cp = robot.lookup("#cbPackages").queryAs(ChoiceBox.class);
        assertEquals(8, cp.getItems().size());

        robot.clickOn("#cbPackages");
        robot.clickOn("Neo");
        robot.clickOn("#btnOk");

        ArrayList<Service> listServices = serviceDAO.services();
        assertEquals(4, listServices.size());

        boolean ok = false;
        for (Service s : listServices)
            if (s.getName().equals("New service") && s.getListPackages().get(0).getName().equals("Neo"))
                ok = true;
         assertTrue(ok);
    }

    @Test
    public void onActionOk2(FxRobot robot) {
        Button btnOk = robot.lookup("#btnOk").queryAs(Button.class);
        assertNotNull(btnOk);

        robot.clickOn("#fldName");
        robot.write("New service");

        ChoiceBox<Package> cp = robot.lookup("#cbPackages").queryAs(ChoiceBox.class);
        assertEquals(8, cp.getItems().size());

        robot.clickOn("#btnOk");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        robot.clickOn("#cbPackages");
        robot.clickOn("Neo");
        robot.clickOn("#btnOk");

        ArrayList<Service> listServices = serviceDAO.services();
        assertEquals(4, listServices.size());

        boolean ok = false;
        for (Service s : listServices)
            if (s.getName().equals("New service") && s.getListPackages().get(0).getName().equals("Neo"))
                ok = true;
        assertTrue(ok);
    }

    @Test
    public void onActionOk3(FxRobot robot) {
        robot.clickOn("Prodaja uređaja");

        ListView<Package> listViewPackages = robot.lookup("#listViewPackages").queryAs(ListView.class);
        assertEquals(2, listViewPackages.getItems().size());

        ChoiceBox<Package> cp = robot.lookup("#cbPackages").queryAs(ChoiceBox.class);
        assertEquals(6, cp.getItems().size());

        robot.clickOn("#cbPackages");
        robot.clickOn("Neo");
        robot.clickOn("#btnOk");

        ArrayList<Service> listServices = serviceDAO.services();
        assertEquals(3, listServices.size());

        boolean ok = false;
        for (Service s : listServices)
            if (s.getName().equals("Prodaja uređaja") && s.getListPackages().get(2).getName().equals("Neo"))
                ok = true;
        assertTrue(ok);
    }

    @Test
    public void onActionOk4(FxRobot robot) {
        Button btnOk = robot.lookup("#btnOk").queryAs(Button.class);
        assertNotNull(btnOk);

        robot.clickOn("Prodaja uređaja");

        ListView<Package> listViewPackages = robot.lookup("#listViewPackages").queryAs(ListView.class);
        assertEquals(2, listViewPackages.getItems().size());

        ChoiceBox<Package> cp = robot.lookup("#cbPackages").queryAs(ChoiceBox.class);
        assertEquals(6, cp.getItems().size());

        robot.clickOn("#fldName");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("Promijenjeno ime usluge");
        robot.clickOn("#btnOk");
        ArrayList<Service> listServices = serviceDAO.services();
        assertEquals(3, listServices.size());

        boolean ok = false;
        for (Service s : listServices)
            if (s.getName().equals("Promijenjeno ime usluge"))
                ok = true;
        assertTrue(ok);
    }

    @Test
    public void onActionArchive(FxRobot robot) {
        ArrayList<Service> listServices = serviceDAO.services();
        assertEquals(3, listServices.size());
        robot.clickOn("Prodaja uređaja");
        robot.clickOn("#btnArchive");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        ListView<Service> serviceListView = robot.lookup("#lstViewServices").queryAs(ListView.class);
        assertEquals(2, serviceListView.getItems().size());

        boolean ok = false;
        for (Service s  : serviceListView.getItems())
            if (s.getName().equals("Prodaja uređaja"))
                ok = true;
        assertFalse(ok);
    }
}
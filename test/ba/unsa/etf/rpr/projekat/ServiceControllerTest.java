package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.DatabaseConnection;
import ba.unsa.etf.rpr.projekat.DAL.ServiceDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class ServiceControllerTest {
    Stage theStage;
    ServiceController ctrl;
    ServiceDAO serviceDAO;

    @Start
    public void start(Stage stage) throws Exception {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();
        serviceDAO = new ServiceDAO();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/service.fxml"));
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


}
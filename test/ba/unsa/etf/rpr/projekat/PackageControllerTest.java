package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.DAL.CustomerDAO;
import ba.unsa.etf.rpr.projekat.DAL.DatabaseConnection;
import ba.unsa.etf.rpr.projekat.DAL.PackageDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;
import java.util.ArrayList;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class PackageControllerTest {
    Stage theStage;
    PackageController ctrl;
    PackageDAO packageDAO;

    @Start
    public void start (Stage stage) throws Exception {
        // Brisemo bazu za slucaj da su prethodni testovi kreirali/brisali drzave
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();
        packageDAO = new PackageDAO();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/package.fxml"));
        stage.setTitle("Packages");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
        stage.toFront();
        theStage = stage;
    }

    @Test
    public void testTableView(FxRobot robot) {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();

        ListView<Package> listViewPackages = robot.lookup("#fldListView").queryAs(ListView.class);
        assertEquals(8, listViewPackages.getItems().size());
    }

    @Test
    void onActionOk(FxRobot robot) {
        Button btnOk = robot.lookup("#btnOk").queryAs(Button.class);
        assertNotNull(btnOk);
        ArrayList<Package> listPackages = packageDAO.packages();
        assertEquals(8, listPackages.size());

        robot.clickOn("#fldName");
        robot.write("Laptop Acer Aspire 3");
        robot.clickOn("#btnOk");


        listPackages = packageDAO.packages();
        assertEquals(9, listPackages.size());

        boolean ok = false;
        for (Package p : listPackages)
            if (p.getName().equals("Laptop Acer Aspire 3"))
                ok = true;
        assertTrue(ok);
    }

    @Test
    void onActionOk2(FxRobot robot) {
        DatabaseConnection.removeInstance();
        File dbfile = new File("base.db");
        dbfile.delete();



    }

}
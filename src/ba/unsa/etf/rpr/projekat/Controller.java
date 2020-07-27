package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Controller {

    public Label dateLabel;
    public Label timeLabel;
    public TextField fldUsername;
    public PasswordField fldPassword;

    @FXML
    public void initialize() {
        dateLabel.setText(LocalDate.now().toString());
        timeLabel.setText(LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond());
    }


    public void onActionLogin(ActionEvent actionEvent) throws IOException {
        if (fldUsername.getText().equals("admin") && fldPassword.getText().equals("admin")) {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/customers.fxml"));
            stage.setTitle("CRM");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.show();
        }
    }
}

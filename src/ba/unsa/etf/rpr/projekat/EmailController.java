package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailController {
    public TextArea fldMessage;
    public Label labelEmail;
    public TextField fldSubject;
    public PasswordField fldYourPassword;
    public TextField fldYourEmail;

    private Customer customer;

    @FXML
    public void initialize() {
        labelEmail.setText(customer.getEmail());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sending Email");
        alert.setHeaderText("Check security");
        alert.setContentText("Allow less secure apps: ON!");
        alert.showAndWait();
    }

    public void onActionSend(ActionEvent actionEvent) {

        String fromEmail = fldYourEmail.getText();
        String password = fldYourPassword.getText();
        String toEmail = customer.getEmail();

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(fromEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject(fldSubject.getText());
            msg.setText(fldMessage.getText());
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Your username or password are incorrect");
            alert.setContentText("Check security on your Email account. Allow less secure apps!");
            alert.showAndWait();
        }
        Stage stage = (Stage) fldMessage.getScene().getWindow();
        stage.close();
    }

    public void onActionCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) fldMessage.getScene().getWindow();
        stage.close();
    }

    public EmailController(Customer customer) {
        this.customer = customer;
    }
}

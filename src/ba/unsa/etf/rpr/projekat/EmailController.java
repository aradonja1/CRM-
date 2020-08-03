package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailController {
    public TextArea fldMessage;
    public Label labelEmail;
    public TextField fldSubject;

    private Customer customer;

    @FXML
    public void initialize() {
        labelEmail.setText(customer.getEmail());
    }

    public void onActionSend(ActionEvent actionEvent) {

        String fromEmail = "ado.radonja@gmail.com";
        String password = "adnan1234";
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
            msg.setSubject("Proba");
            msg.setText(fldMessage.getText());
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) fldMessage.getScene().getWindow();
        stage.close();
    }

    public void onActionCancel(ActionEvent actionEvent) {
    }

    public EmailController(Customer customer) {
        this.customer = customer;
    }
}

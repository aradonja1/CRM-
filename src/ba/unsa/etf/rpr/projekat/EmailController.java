package ba.unsa.etf.rpr.projekat;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailController  {
    public TextArea fldMessage;
    public Label labelEmail;
    public TextField fldSubject;
    public PasswordField fldYourPassword;
    public TextField fldYourEmail;

    private ObservableList<Customer> listCustomers;

    @FXML
    public void initialize() {
        String toEmail = "";
        for (int i = 0; i < listCustomers.size() - 1; i++) {
            toEmail += listCustomers.get(i).getEmail()+"\n";
        }
        toEmail+= listCustomers.get(listCustomers.size() - 1).getEmail();
        labelEmail.setText(toEmail);

        fldYourEmail.setText("crmetfrpr2020@gmail.com");
        fldYourPassword.setText("RazvojProgramskihRjesenja2020");
        fldMessage.setText("Razvoj programskih rješenja 2019/2020\nCustomer Relationship Managment(CRM)\nTelekom Slovenije SL\n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sending Email");
        alert.setHeaderText("Check security");
        alert.setContentText("Allow less secure apps: ON!");
        alert.showAndWait();
    }

    public void onActionSend(ActionEvent actionEvent) {
        String fromEmail = fldYourEmail.getText();
        String password = fldYourPassword.getText();
        String toEmail = "";


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
            InternetAddress[] mailAddress_TO = new InternetAddress [listCustomers.size()] ;
            for(int i = 0; i < listCustomers.size(); i++)
                mailAddress_TO[i] = new InternetAddress(listCustomers.get(i).getEmail());

            msg.addRecipients(Message.RecipientType.TO, mailAddress_TO);
            msg.setSubject(fldSubject.getText());
            msg.setText(fldMessage.getText());

            Transport.send(msg);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Sending Email");
            alert.setHeaderText("Message sent successfully");
            alert.showAndWait();

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

    public EmailController(ObservableList<Customer> listCustomers) {
        this.listCustomers = listCustomers;
    }
}


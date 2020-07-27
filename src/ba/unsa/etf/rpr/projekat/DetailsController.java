package ba.unsa.etf.rpr.projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DetailsController {

    public TextField fldName;
    public TextField fldSurname;
    public TextField fldEmail;
    public ChoiceBox cbPackage;
    public DatePicker dpBegin;
    public DatePicker dpEnd;
    public TextField fldAdress;


    @FXML
    public void initialize() {
        fldName.textProperty().addListener((obs, oldName, newName) -> {
            if (fldName.getText().isEmpty() || !isAlpha(fldName.getText())) {
                fldName.getStyleClass().removeAll("correctField");
                fldName.getStyleClass().add("incorrectField");
            } else {
                fldName.getStyleClass().removeAll("incorrectField");
            }
        });

        fldSurname.textProperty().addListener((obs, oldSurname, newSurname) -> {
            if (fldSurname.getText().isEmpty() || !isAlpha(fldSurname.getText())) {
                fldSurname.getStyleClass().removeAll("correctField");
                fldSurname.getStyleClass().add("incorrectField");
            } else {
                fldSurname.getStyleClass().removeAll("incorrectField");
            }
        });

        fldAdress.textProperty().addListener((obs, oldAdress, newAdress) -> {
            if (fldAdress.getText().isEmpty() || !isAlpha(fldAdress.getText()) || !isDigit(fldAdress.getText())) {
                fldAdress.getStyleClass().removeAll("correctField");
                fldAdress.getStyleClass().add("incorrectField");
            } else {
                fldAdress.getStyleClass().removeAll("incorrectField");
            }
        });

        dpBegin.valueProperty().addListener(((observableValue, localDate, t1) -> {
            if (dpEnd.getValue() != null) {
                if (dpBegin.getValue().isAfter(dpEnd.getValue())) {
                    dpBegin.getEditor().getStyleClass().removeAll("correctField");
                    dpBegin.getEditor().getStyleClass().add("incorrectField");
                } else {
                    dpBegin.getEditor().getStyleClass().removeAll("incorrectField");
                }
            }
        }));

        dpEnd.valueProperty().addListener(((observableValue, localDate, t1) -> {
            if (dpBegin.getValue() != null) {
                if (dpEnd.getValue().isBefore(dpBegin.getValue())) {
                    dpEnd.getEditor().getStyleClass().removeAll("correctField");
                    dpEnd.getEditor().getStyleClass().add("incorrectField");
                } else {
                    dpEnd.getEditor().getStyleClass().removeAll("incorrectField");
                }
            }
        }));
    }

    private boolean isDigit(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!(Character.isDigit(c))) {
                return false;
            }
        }
        return true;
    }

    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!(Character.isLetter(c) || c == ' ' || c == '-')) {
                return false;
            }
        }
        return true;
    }

    public void onActionRecords(ActionEvent actionEvent) {
    
    }

    public void onActionSave(ActionEvent actionEvent) {

    }
}

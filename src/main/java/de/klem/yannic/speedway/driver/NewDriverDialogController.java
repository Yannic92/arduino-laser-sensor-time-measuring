package de.klem.yannic.speedway.driver;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class NewDriverDialogController {

    @FXML
    private TextField lastNameInput;

    @FXML
    private TextField firstNameInput;

    @FXML
    private TextField clubInput;
    @FXML
    private TextField startNumberInput;

    @FXML
    private TextField classInput;

    @FXML
    private Button createButton;

    @FXML
    private void enableCreateButton() {
        createButton.setDisable(!formIsValid());
    }

    private boolean formIsValid() {
        if(lastNameInput.getText().isEmpty()) {
            return false;
        }
        if(firstNameInput.getText().isEmpty()) {
            return false;
        }
        if(clubInput.getText().isEmpty()) {
            return false;
        }
        if(startNumberInput.getText().isEmpty()) {
            return false;
        }
        if(classInput.getText().isEmpty()) {
            return false;
        }

        return true;
    }
}

package de.klem.yannic.speedway.driver;

import de.klem.yannic.speedway.ui.SpeedwayController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewDriverDialogController implements SpeedwayController, Initializable {

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
    private Button createAndCloseButton;

    @FXML
    private void enableCreateButton() {
        createButton.setDisable(!formIsValid());
        createAndCloseButton.setDisable(!formIsValid());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createAndCloseButton.getParent().addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                createButton.fire();
                ev.consume();
            }
        });
    }

    private ObservableList<Driver> driversList;

    private boolean formIsValid() {
        if (lastNameInput.getText().isEmpty()) {
            return false;
        }
        if (firstNameInput.getText().isEmpty()) {
            return false;
        }
        if (clubInput.getText().isEmpty()) {
            return false;
        }
        if (startNumberInput.getText().isEmpty()) {
            return false;
        }
        if (classInput.getText().isEmpty()) {
            return false;
        }

        return true;
    }

    void setDriversList(ObservableList<Driver> driversList) {
        this.driversList = driversList;
    }

    @FXML
    private void addDriver() {
        addDriverToList();
    }

    @FXML
    private void addDriverAndClose(ActionEvent event) {
        addDriverToList();
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    private void addDriverToList() {
        String firstName = firstNameInput.getText();
        String lastName = lastNameInput.getText();
        String club = clubInput.getText();
        String startNumber = startNumberInput.getText();
        String driverClass = classInput.getText();

        this.driversList.add(new Driver(firstName, lastName, club, startNumber, driverClass));
        reset();
    }

    private void reset() {
        firstNameInput.clear();
        lastNameInput.clear();
        clubInput.clear();
        startNumberInput.clear();
        classInput.clear();
        createButton.setDisable(true);
        createAndCloseButton.setDisable(true);
        lastNameInput.requestFocus();
    }

    @Override
    public void exit() {

    }
}

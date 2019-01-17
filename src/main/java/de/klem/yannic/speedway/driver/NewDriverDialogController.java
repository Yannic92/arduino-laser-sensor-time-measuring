package de.klem.yannic.speedway.driver;

import de.klem.yannic.speedway.ui.SpeedwayController;
import de.klem.yannic.speedway.ui.TextFieldValidator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NewDriverDialogController implements SpeedwayController, Initializable {

    private static final String START_NUMBER_NOT_UNIQUE = "Startnummer ist bereits vergeben.";

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

    private final List<TextField> allTextFields = new ArrayList<>();
    private final List<TextFieldValidator> validators = new ArrayList<>();

    private void setDisabledOfCreateButtons(final boolean disabled) {
        createButton.setDisable(disabled);
        createAndCloseButton.setDisable(disabled);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createAndCloseButton.getParent().addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                if (ev.getTarget().equals(createAndCloseButton)) {
                    createAndCloseButton.fire();
                } else {
                    createButton.fire();
                }
                ev.consume();
            }
        });

        allTextFields.add(firstNameInput);
        allTextFields.add(lastNameInput);
        allTextFields.add(clubInput);
        allTextFields.add(classInput);
        allTextFields.add(startNumberInput);

        allTextFields.forEach(textField -> {
            validators.add(TextFieldValidator.notEmptyValidator(textField));
        });


        validators.add(new TextFieldValidator(
                startNumberInput,
                text -> !startNumberAlreadyExists(text),
                START_NUMBER_NOT_UNIQUE));

        validators.forEach(TextFieldValidator::enable);

        enableButtonListener();
    }

    private boolean startNumberAlreadyExists(final String startNumber) {
        return !driversList.filtered(driver -> startNumber.equals(driver.getStartNumber())).isEmpty();
    }

    private void enableButtonListener() {
        for (TextField textField : allTextFields) {
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                setDisabledOfCreateButtons(anyValidatorIsFalse());
            });
        }
    }

    private boolean anyValidatorIsFalse() {
        return validators.stream().anyMatch(validator -> !validator.isValid());
    }

    private ObservableList<Driver> driversList;

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
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
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
        validators.forEach(TextFieldValidator::disable);
        allTextFields.forEach(TextInputControl::clear);
        lastNameInput.requestFocus();
        validators.forEach(TextFieldValidator::enable);
    }

    @Override
    public void exit() {

    }
}

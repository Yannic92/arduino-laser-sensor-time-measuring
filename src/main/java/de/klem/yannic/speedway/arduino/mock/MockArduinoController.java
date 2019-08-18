package de.klem.yannic.speedway.arduino.mock;

import de.klem.yannic.speedway.utils.ui.SpeedwayController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MockArduinoController implements SpeedwayController {

    @FXML
    private Button triggerLightBarrierButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        triggerLightBarrierButton.setOnAction(event -> MockArduino.INSTANCE.simulateTick());
    }
}

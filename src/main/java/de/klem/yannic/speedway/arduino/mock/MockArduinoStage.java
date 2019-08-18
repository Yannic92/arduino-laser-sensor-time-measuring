package de.klem.yannic.speedway.arduino.mock;

import de.klem.yannic.speedway.utils.ui.SpeedwayStage;
import javafx.stage.Stage;

import java.io.IOException;

public class MockArduinoStage extends SpeedwayStage {

    MockArduinoStage() throws IOException {

        super(new Stage(), "views/arduino/mockArduino.fxml", "Lightbarrier simulator", "icons/racing-flag.png");
        stage.show();
    }

    @Override
    protected void exit() {

    }
}
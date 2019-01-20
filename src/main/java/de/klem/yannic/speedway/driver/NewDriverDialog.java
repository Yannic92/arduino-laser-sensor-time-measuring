package de.klem.yannic.speedway.driver;

import de.klem.yannic.speedway.utils.ui.SpeedwayStage;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

class NewDriverDialog extends SpeedwayStage {

    NewDriverDialog(final Parent parent) throws IOException {
        super(new Stage(), "views/driver/newDriver.fxml", "Neuer Fahrer", "icons/driver.png");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parent.getScene().getWindow());
        stage.setMaxWidth(358);
        stage.show();

        stage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                stage.close();
            }
        });
    }

    @Override
    protected void exit() {
        // No Action required;
    }
}

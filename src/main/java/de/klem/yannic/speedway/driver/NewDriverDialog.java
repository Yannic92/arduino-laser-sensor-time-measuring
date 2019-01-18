package de.klem.yannic.speedway.driver;

import de.klem.yannic.speedway.utils.ui.SpeedwayStage;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NewDriverDialog extends SpeedwayStage {

    public NewDriverDialog(final Parent parent, final ObservableList<Driver> driversList) throws IOException {
        super(new Stage(), "views/newDriver/newDriver.fxml", "Neuer Fahrer", "icons/driver.png");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parent.getScene().getWindow());
        stage.setMaxWidth(358);
        stage.show();
        ((NewDriverDialogController) controller).setDriversList(driversList);

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

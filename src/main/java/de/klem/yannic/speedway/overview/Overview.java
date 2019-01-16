package de.klem.yannic.speedway.overview;

import de.klem.yannic.speedway.ui.SpeedwayStage;
import javafx.stage.Stage;

import java.io.IOException;

public class Overview extends SpeedwayStage {

    public Overview(final Stage primaryStage) throws IOException {
        super(primaryStage, "speedway.fxml", "Speedway", "go-kart.png");
        maximize();
        primaryStage.show();
    }

    @Override
    protected void exit() {
        controller.exit();
    }


}

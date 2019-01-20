package de.klem.yannic.speedway;

import de.klem.yannic.speedway.utils.ui.SpeedwayStage;
import javafx.stage.Stage;

import java.io.IOException;

public class MainView extends SpeedwayStage {

    MainView(final Stage primaryStage) throws IOException {
        super(primaryStage, "views/mainView.fxml", "Speedway", "icons/go-kart.png");
        maximize();
        primaryStage.show();
    }

    @Override
    protected void exit() {

    }
}

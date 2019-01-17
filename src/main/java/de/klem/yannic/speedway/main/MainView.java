package de.klem.yannic.speedway.main;

import de.klem.yannic.speedway.ui.SpeedwayStage;
import javafx.stage.Stage;

import java.io.IOException;

public class MainView extends SpeedwayStage {

    public MainView(final Stage primaryStage) throws IOException {
        super(primaryStage, "views/mainView/mainView.fxml", "Speedway", "icons/go-kart.png");
        maximize();
        primaryStage.show();
    }

    @Override
    protected void exit() {
        controller.exit();
    }


}

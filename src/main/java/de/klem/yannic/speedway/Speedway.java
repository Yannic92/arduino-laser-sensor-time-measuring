package de.klem.yannic.speedway;


import de.klem.yannic.speedway.arduino.Arduino;
import de.klem.yannic.speedway.utils.async.Async;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class Speedway extends Application {

    static {
        InputStream stream = Speedway.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new MainView(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
        Async.shutdown();
        Arduino.getSingletonInstance().disconnect();
        System.exit(0);
    }
}

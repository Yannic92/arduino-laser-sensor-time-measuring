package de.klem.yannic.speedway;


import de.klem.yannic.speedway.main.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;

public class Speedway extends Application {

    public static final ExecutorService executor;

    static {
        executor = Executors.newCachedThreadPool();
        InputStream stream = Speedway.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL resource = getClass().getClassLoader().getResource("fxml/speedway.fxml");
        new MainView(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
        executor.shutdown();
//        System.exit(0);
    }
}

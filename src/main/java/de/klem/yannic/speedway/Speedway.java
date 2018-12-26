package de.klem.yannic.speedway;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Speedway extends Application {

    static {
        InputStream stream = Speedway.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlLocation = getClass().getClassLoader().getResource("speedway.fxml");
        Parent root = FXMLLoader.load(fxmlLocation);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setTitle("Speedway");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        String style = getClass().getClassLoader().getResource("style.css").toExternalForm();
        primaryStage.getScene().getStylesheets().add(style);
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("go-kart.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

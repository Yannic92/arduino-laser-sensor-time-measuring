package de.klem.yannic.speedway.timetable;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class TimeTable {

    public TimeTable() {
        URL fxmlLocation = getClass().getClassLoader().getResource("timetable.fxml");
        try {
            Parent root = FXMLLoader.load(fxmlLocation);
            Stage stage = new Stage();
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            stage.setTitle("Zeiten");
            stage.setScene(new Scene(root, 1000, 800));
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            String style = getClass().getClassLoader().getResource("style.css").toExternalForm();
            stage.getScene().getStylesheets().add(style);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("time.png"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

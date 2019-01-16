package de.klem.yannic.speedway.driver;

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

public class NewDriverDialog {

    public NewDriverDialog() {
        URL fxmlLocation = getClass().getClassLoader().getResource("fxml/newDriver.fxml");
        try {
            Parent root = FXMLLoader.load(fxmlLocation);
            Stage stage = new Stage();
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            stage.setTitle("Neuer Fahrer");
            stage.setScene(new Scene(root, 250, 371));
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icons/driver.png"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package de.klem.yannic.speedway.utils.ui;

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

public abstract class SpeedwayStage {
    protected final Stage stage;
    protected final SpeedwayController controller;

    protected SpeedwayStage(final Stage stage, final String fxmlFile, final String title, final String iconFile) throws IOException {
        this.stage = stage;
        this.stage.setOnHidden((event) -> {
            exit();
        });
        this.stage.setTitle(title);
        this.stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(iconFile))));
        URL fxmlFileUrl = getClass().getClassLoader().getResource(fxmlFile);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxmlFileUrl);
        Parent root = fxmlLoader.load();
        this.controller = fxmlLoader.getController();
        this.stage.setScene(new Scene(root));
    }

    protected void maximize() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
    }

    public void hide() {
        stage.hide();
    }

    protected abstract void exit();
}

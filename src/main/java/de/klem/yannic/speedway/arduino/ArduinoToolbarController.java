package de.klem.yannic.speedway.arduino;

import de.klem.yannic.speedway.Speedway;
import de.klem.yannic.speedway.arduino.event.ConnectivityEvent;
import de.klem.yannic.speedway.MainViewController;
import de.klem.yannic.speedway.utils.ui.SpeedwayController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ArduinoToolbarController implements SpeedwayController {

    private static final Node loadingIcon;

    private static final Node connectedIcon;

    private static final Node disconnectedIcon;

    static {
        loadingIcon = new ImageView(new Image(
                Objects.requireNonNull(MainViewController.class.getClassLoader().getResourceAsStream("icons/loading.png"))
        ));
        ((ImageView) loadingIcon).setFitHeight(25);
        ((ImageView) loadingIcon).setFitWidth(25);

        connectedIcon = new ImageView(new Image(
                Objects.requireNonNull(MainViewController.class.getClassLoader().getResourceAsStream("icons/connected.png"))
        ));
        ((ImageView) connectedIcon).setFitHeight(25);
        ((ImageView) connectedIcon).setFitWidth(25);

        disconnectedIcon = new ImageView(new Image(
                Objects.requireNonNull(MainViewController.class.getClassLoader().getResourceAsStream("icons/disconnected.png"))
        ));
        ((ImageView) disconnectedIcon).setFitWidth(25);
        ((ImageView) disconnectedIcon).setFitHeight(25);
    }

    @FXML
    private Button connectButton;

    private void setDisconnected(final ConnectivityEvent connectivityEvent) {
        Platform.runLater(() -> {
            connectButton.setOnAction((event) ->
                    Speedway.executor.submit(() -> connectivityEvent.getSource().connect()));
            connectButton.setText("Arduino verbinden");
            connectButton.setGraphic(disconnectedIcon);
            connectButton.setDisable(false);
        });
    }

    private void setConnected(final ConnectivityEvent connectivityEvent) {
        Platform.runLater(() -> {
            connectButton.setOnAction((event ->
                    Speedway.executor.submit(() -> connectivityEvent.getSource().disconnect())));
            connectButton.setText("Arduino trennen");
            connectButton.setGraphic(connectedIcon);
            connectButton.setDisable(false);
        });
    }


    private void setConnecting(final ConnectivityEvent connectivityEvent) {
        connectButton.setDisable(true);
        Platform.runLater(() -> {
            connectButton.setText("Verbinde...");
            connectButton.setGraphic(loadingIcon);
            connectButton.setOnAction(event -> {
                //Do Nothing
            });
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final Arduino arduino = Arduino.INSTANCE;

        arduino.addEventHandler(ConnectivityEvent.CONNECTED_TYPE, this::setConnected);
        arduino.addEventHandler(ConnectivityEvent.DISCONNECTED_TYPE, this::setDisconnected);
        arduino.addEventHandler(ConnectivityEvent.CONNECTING_TYPE, this::setConnecting);

        Speedway.executor.submit(arduino::connect);
    }
}

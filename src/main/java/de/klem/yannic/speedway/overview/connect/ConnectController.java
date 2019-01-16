package de.klem.yannic.speedway.overview.connect;

import de.klem.yannic.speedway.Speedway;
import de.klem.yannic.speedway.arduino.Arduino;
import de.klem.yannic.speedway.arduino.event.ConnectivityEvent;
import de.klem.yannic.speedway.overview.OverviewController;
import de.klem.yannic.speedway.ui.SpeedwayController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class ConnectController implements SpeedwayController {

    private static final Node loadingIcon;

    private static final Node connectedIcon;

    private static final Node disconnectedIcon;

    static {
        loadingIcon = new ImageView(new Image(
                Objects.requireNonNull(OverviewController.class.getClassLoader().getResourceAsStream("loading.png"))
        ));
        ((ImageView) loadingIcon).setFitHeight(25);
        ((ImageView) loadingIcon).setFitWidth(25);

        connectedIcon = new ImageView(new Image(
                Objects.requireNonNull(OverviewController.class.getClassLoader().getResourceAsStream("connected.png"))
        ));
        ((ImageView) connectedIcon).setFitHeight(25);
        ((ImageView) connectedIcon).setFitWidth(25);

        disconnectedIcon = new ImageView(new Image(
                Objects.requireNonNull(OverviewController.class.getClassLoader().getResourceAsStream("disconnected.png"))
        ));
        ((ImageView) disconnectedIcon).setFitWidth(25);
        ((ImageView) disconnectedIcon).setFitHeight(25);
    }

    private final Button connectButton;
    private final Arduino arduino;

    public ConnectController(final Arduino arduino, final Button connectButton) {
        this.arduino = arduino;
        this.connectButton = connectButton;

        arduino.addEventHandler(ConnectivityEvent.CONNECTED_TYPE, this::setConnected);
        arduino.addEventHandler(ConnectivityEvent.DISCONNECTED_TYPE, this::setDisconnected);
        arduino.addEventHandler(ConnectivityEvent.CONNECTING_TYPE, this::setConnecting);

        Speedway.executor.submit(arduino::connect);
    }

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
    public void exit() {
        this.arduino.disconnect();
    }
}

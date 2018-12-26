package de.klem.yannic.speedway;

import de.klem.yannic.speedway.arduino.Arduino;
import de.klem.yannic.speedway.arduino.ConnectedArduino;
import de.klem.yannic.speedway.measure.LapTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

class ConnectController {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private static final Node loadingIcon;

    private static final Node connectedIcon;

    private static final Node disconnectedIcon;

    static {
        loadingIcon = new ImageView(new Image(
                Objects.requireNonNull(Controller.class.getClassLoader().getResourceAsStream("loading.png"))
        ));
        ((ImageView) loadingIcon).setFitHeight(25);
        ((ImageView) loadingIcon).setFitWidth(25);

        connectedIcon = new ImageView(new Image(
                Objects.requireNonNull(Controller.class.getClassLoader().getResourceAsStream("connected.png"))
        ));
        ((ImageView) connectedIcon).setFitHeight(25);
        ((ImageView) connectedIcon).setFitWidth(25);

        disconnectedIcon = new ImageView(new Image(
                Objects.requireNonNull(Controller.class.getClassLoader().getResourceAsStream("disconnected.png"))
        ));
        ((ImageView) disconnectedIcon).setFitWidth(25);
        ((ImageView) disconnectedIcon).setFitHeight(25);
    }

    private final ComboBox<String> portSelector;
    private final Button connectButton;

    ConnectController(final ComboBox<String> portSelector, final Button connectButton) {
        this.connectButton = connectButton;
        this.portSelector = portSelector;
        setDisconnected(new Arduino());
    }

    private void setConnected(final ConnectedArduino arduino) {
        connectButton.setText("Arduino trennen");
        connectButton.setGraphic(connectedIcon);
        connectButton.setOnAction(new DisconnectHandler(arduino));
        connectButton.setDisable(false);
    }

    private void setDisconnected(final Arduino arduino) {
        connectButton.setText("Arduino verbinden");
        connectButton.setGraphic(disconnectedIcon);
        connectButton.setOnAction(new ConnectHandler(arduino));
        connectButton.setDisable(false);
    }

    private void setConnecting() {
        connectButton.setText("Verbinde...");
        connectButton.setGraphic(loadingIcon);
        connectButton.setOnAction(event -> {
            //Do Nothing
        });
        connectButton.setDisable(true);
    }

    void refreshPorts() {
        List<String> availablePorts = Arduino.SerialFactory.getAvailablePorts();
        portSelector.getItems().addAll(availablePorts);
    }

    private class DisconnectHandler implements EventHandler<ActionEvent> {

        private final ConnectedArduino arduino;

        private DisconnectHandler(ConnectedArduino arduino) {
            this.arduino = arduino;
        }

        @Override
        public void handle(ActionEvent event) {
            arduino.disconnect();
            setDisconnected(new Arduino());
        }
    }

    private class ConnectHandler implements EventHandler<ActionEvent> {

        private final Arduino arduino;

        private ConnectHandler(Arduino arduino) {
            this.arduino = arduino;
        }

        @Override
        public void handle(ActionEvent event) {
            LapTimer lapTimerImpl = new LapTimer(lapDuration ->
                    logger.info(String.format("Lap finished in %f seconds.", lapDuration.toMillis() / 1000.0)));
            String selectedPort = portSelector.getSelectionModel().getSelectedItem();

            setConnecting();
            Optional<ConnectedArduino> connectedArduinoOpt = arduino.connect(selectedPort);
            if (connectedArduinoOpt.isPresent()) {
                ConnectedArduino connectedArduino = connectedArduinoOpt.get();
                connectedArduino.onLapTick(lapTimerImpl);
                setConnected(connectedArduino);
            } else {
                setDisconnected(arduino);
            }

        }
    }
}

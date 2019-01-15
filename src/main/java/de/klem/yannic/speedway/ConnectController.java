package de.klem.yannic.speedway;

import de.klem.yannic.speedway.arduino.Arduino;
import de.klem.yannic.speedway.arduino.ArduinoSerial;
import de.klem.yannic.speedway.arduino.ConnectedArduino;
import de.klem.yannic.speedway.measure.LapTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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

    private final Button connectButton;

    ConnectController(final Button connectButton) {
        this.connectButton = connectButton;
        CompletableFuture<Void> disconnected = setDisconnected(new Arduino());
        disconnected.thenAccept((voidValue) -> {
            this.connectButton.fire();
        });
    }

    private CompletableFuture<Void> setConnected(final ConnectedArduino arduino) {
        CompletableFuture<Void> connectedFuture = new CompletableFuture<>();
        Platform.runLater(() -> {
            connectButton.setText("Arduino trennen");
            connectButton.setGraphic(connectedIcon);
            connectButton.setOnAction(new DisconnectHandler(arduino));
            connectButton.setDisable(false);
            connectedFuture.complete(null);
        });
        return connectedFuture;
    }

    private CompletableFuture<Void> setDisconnected(final Arduino arduino) {
        CompletableFuture<Void> disconnectedFuture = new CompletableFuture<>();
        Platform.runLater(() -> {
            connectButton.setText("Arduino verbinden");
            connectButton.setGraphic(disconnectedIcon);
            connectButton.setOnAction(new ConnectHandler(arduino));
            connectButton.setDisable(false);
            disconnectedFuture.complete(null);
        });
        return disconnectedFuture;
    }

    private CompletableFuture<Void> setConnecting() {
        CompletableFuture<Void> connectingFuture = new CompletableFuture<>();
        Platform.runLater(() -> {
            connectButton.setText("Verbinde...");
            connectButton.setGraphic(loadingIcon);
            connectButton.setOnAction(event -> {
                //Do Nothing
            });
            connectingFuture.complete(null);
        });
        return connectingFuture;
    }

    private class DisconnectHandler implements EventHandler<ActionEvent> {

        private final ConnectedArduino arduino;

        private DisconnectHandler(ConnectedArduino arduino) {
            this.arduino = arduino;
        }

        @Override
        public void handle(ActionEvent event) {
            connectButton.setDisable(true);
            new Thread(this::disconnectFromArduino).start();
        }

        private void disconnectFromArduino() {
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
            connectButton.setDisable(true);
            new Thread(this::connectToArduino).start();
        }

        private void connectToArduino() {
            setConnecting();
            Optional<ConnectedArduino> connectedArduinoOpt = performConnecting();

            if (connectedArduinoOpt.isPresent()) {
                logger.info("Connected to Arduino.");
                LapTimer lapTimerImpl = new LapTimer(lapDuration ->
                        logger.info(String.format("Lap finished in %f seconds.", lapDuration.toMillis() / 1000.0)));
                ConnectedArduino connectedArduino = connectedArduinoOpt.get();
                connectedArduino.onLapTick(lapTimerImpl);
                setConnected(connectedArduino);
            } else {
                logger.info("Could not connect to Arduino.");
                setDisconnected(arduino);
            }
        }

        private Optional<ConnectedArduino> performConnecting() {
            List<String> availablePorts = ArduinoSerial.getAvailablePorts();
            for (String port : availablePorts) {
                Optional<ConnectedArduino> connectedArduino = arduino.connect(port);

                if (connectedArduino.isPresent()) {
                    return connectedArduino;
                }
            }
            return Optional.empty();
        }
    }
}

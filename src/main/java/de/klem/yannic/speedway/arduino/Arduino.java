package de.klem.yannic.speedway.arduino;

import de.klem.yannic.speedway.arduino.event.ConnectivityEvent;
import de.klem.yannic.speedway.async.AbstractObservable;
import de.klem.yannic.speedway.measure.LapTickHandler;
import javafx.event.Event;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.TooManyListenersException;
import java.util.logging.Logger;

public class Arduino extends AbstractObservable<ConnectivityEvent> {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static final Long secondsToLock = 5L;
    private ArduinoSerial serial;
    private Instant lastTick;

    public boolean connect() {

        emit(ConnectivityEvent.CONNECTING(this));

        if (isConnected()) {
            return true;
        }

        List<String> availablePorts = ArduinoSerial.getAvailablePorts();
        for (String comPort : availablePorts) {
            boolean connected = performConnect(comPort);
            if (connected) {
                emit(ConnectivityEvent.CONNECTED(this));
                return true;
            }
        }
        emit(ConnectivityEvent.DISCONNECTED(this));
        return false;


    }

    public boolean isConnected() {
        return this.serial != null && this.serial.isConnected();
    }

    public void disconnect() {
        logger.info("Disconnect from Arduino.");

        if (!isConnected()) {
            return;
        }

        this.serial.disconnect();
        this.serial = null;
        emit(ConnectivityEvent.DISCONNECTED(this));
    }

    private boolean performConnect(final String comPort) {

        this.serial = ArduinoSerial.getSerial(comPort);

        logger.info("Trying to connect to Arduino.");
        boolean connected = this.serial.connect();

        if (!connected) {
            return false;
        }
        boolean handshakeSucceeded = performHandshake();

        if (handshakeSucceeded) {
            return true;
        }

        serial.disconnect();
        this.serial = null;
        return false;
    }

    private boolean performHandshake() {
        Objects.requireNonNull(this.serial);
        logger.info("Performing handshake.");

        serial.clearInputStream();
        serial.write("Reset");

        String waitingForHandshake = serial.readWithTimeout(serial, 23).orElse("");

        if (!"Waiting for Handshake\r\n".equals(waitingForHandshake)) {
            return false;
        }

        serial.write("Arduino\n");

        String uno = serial.readWithTimeout(serial, 5).orElse("");

        if (!"Uno\r\n".equals(uno)) {
            return false;
        }

        serial.write("Go\n");

        logger.info("Completed Handshake successfully.");
        return true;
    }

    public void onLapTick(final LapTickHandler lapTickHandler) {
        try {
            this.serial.addEventListener(serialPortEvent -> {
                Instant now = Instant.now();
                if (lastTick == null) {
                    logger.info("First lap ticked");
                    lastTick = now;
                    lapTickHandler.tick(lastTick);
                } else if (lastTick.isBefore(now.minusSeconds(secondsToLock))) {
                    logger.info("Lap ticked");
                    lastTick = now;
                    lapTickHandler.tick(now);
                }
                this.serial.clearInputStream();
            });
        } catch (TooManyListenersException e) {
            throw new TooManyListenersRuntimeException(e);
        }
    }
}

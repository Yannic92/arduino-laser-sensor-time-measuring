package de.klem.yannic.speedway.arduino;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.logging.Logger;

public class Arduino {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public Optional<ConnectedArduino> connect(final String port) {
        final ArduinoSerial serial = ArduinoSerial.getSerial(port);
        logger.info("Trying to connect to Arduino.");
        boolean connected = serial.connect();
        if (connected) {
            Optional<ConnectedArduino> connectedArduino = performHandshake(serial);
            if (connectedArduino.isPresent()) {
                return connectedArduino;
            } else {
                serial.disconnect();
            }
        }

        return Optional.empty();
    }

    private Optional<ConnectedArduino> performHandshake(final ArduinoSerial serial) {
        logger.info("Performing handshake.");

        serial.clearInputStream();
        serial.write("Reset");

        String waitingForHandshake = serial.readWithTimeout(serial, 23).orElse("");

        if (!"Waiting for Handshake\r\n".equals(waitingForHandshake)) {
            return Optional.empty();
        }

        serial.write("Arduino\n");

        String uno = serial.readWithTimeout(serial, 5).orElse("");

        if (!"Uno\r\n".equals(uno)) {
            return Optional.empty();
        }

        serial.write("Go\n");

        logger.info("Completed Handshake successfully.");
        return Optional.of(new ConnectedArduino(serial));
    }
}

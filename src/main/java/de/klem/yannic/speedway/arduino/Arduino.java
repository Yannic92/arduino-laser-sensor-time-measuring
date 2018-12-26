package de.klem.yannic.speedway.arduino;

import gnu.io.NRSerialPort;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class Arduino {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public Optional<ConnectedArduino> connect(final String port) {
        final NRSerialPort serial = SerialFactory.getSerial(port);
        logger.info("Trying to connect to Arduino.");
        boolean connected = serial.connect();
        if (connected) {
            logger.info("Connected to Arduino successfully.");
            return Optional.of(new ConnectedArduino(serial));
        } else {
            logger.info("Could not connect to Arduino.");
            return Optional.empty();
        }
    }

    public static class SerialFactory {
        private static final Integer baudRate = 115200;

        public static List<String> getAvailablePorts() {
            List<String> availablePorts = new ArrayList<>(NRSerialPort.getAvailableSerialPorts());
            Collections.sort(availablePorts);
            return availablePorts;
        }

        private static NRSerialPort getSerial(final String port) {
            SerialFactory.validatePort(port);
            return new NRSerialPort(port, baudRate);
        }

        private static void validatePort(final String port) {
            List<String> availablePorts = getAvailablePorts();
            if (!availablePorts.contains(port)) {
                throw new PortUnavailableException(port, availablePorts);
            }
        }
    }
}

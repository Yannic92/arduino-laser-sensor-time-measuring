package de.klem.yannic.speedway.arduino;

import de.klem.yannic.speedway.Speedway;
import gnu.io.NRSerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.logging.Logger;

class ArduinoSerial extends NRSerialPort {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static final Integer baudRate = 9600;
    private final String port;

    private ArduinoSerial(String port, int baud) {
        super(port, baud);
        this.port = port;
    }

    static List<String> getAvailablePorts() {
        List<String> availablePorts = new ArrayList<>(NRSerialPort.getAvailableSerialPorts());
        Collections.sort(availablePorts);
        return availablePorts;
    }

    static ArduinoSerial getSerial(final String port) {
        ArduinoSerial.validatePort(port);
        return new ArduinoSerial(port, baudRate);
    }

    private static void validatePort(final String port) {
        List<String> availablePorts = getAvailablePorts();
        if (!availablePorts.contains(port)) {
            throw new PortUnavailableException(port, availablePorts);
        }
    }

    void clearInputStream() {
        InputStream inputStream = getInputStream();
        try {
            while (inputStream.available() > 0) {
                inputStream.read();
            }
        } catch (IOException e) {
            // Do nothing
        }
    }

    Optional<String> readWithTimeout(final ArduinoSerial serial, final int numberOfExpectedBytes) {
        final Future<String> serialInputString = Speedway.executor.submit(
                () -> performReading(serial, numberOfExpectedBytes));
        try {
            return Optional.of(serialInputString.get(5, TimeUnit.SECONDS));
        } catch (InterruptedException | TimeoutException | ExecutionException ex) {
            return Optional.empty();
        } finally {
            serialInputString.cancel(true);
        }
    }

    private static String performReading(final ArduinoSerial serial, final int numberOfExpectedBytes)
            throws IOException {

        InputStream inputStream = serial.getInputStream();
        while (inputStream.available() < numberOfExpectedBytes) {
            //Wait for available bytes
            try {
                Thread.sleep(100);
                if (Speedway.executor.isShutdown()) {
                    return "";
                }
            } catch (InterruptedException e) {
                return "";
            }
        }
        byte[] serialInputBytes = new byte[numberOfExpectedBytes];
        inputStream.read(serialInputBytes);
        return new String(serialInputBytes);
    }

    void write(final String stringToWrite) {
        try {
            getOutputStream().write(stringToWrite.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean connect() {
        boolean connected = super.connect();
        if (connected) {
            logger.info(String.format("Connected to COM Port '%s' successfully.", port));
        } else {
            logger.info(String.format("Could not connect to COM Port '%s'.", port));
        }
        return connected;
    }

    @Override
    public void disconnect() {
        logger.info(String.format("Disconnecting from COM Port '%s'.", port));
        super.disconnect();
    }
}

package de.klem.yannic.speedway.arduino;

import de.klem.yannic.speedway.utils.async.Async;
import gnu.io.NRSerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
        logger.info("Looking for available ports.");
        List<String> availablePorts = new ArrayList<>(NRSerialPort.getAvailableSerialPorts());
        Collections.sort(availablePorts);
        return availablePorts;
    }

    static ArduinoSerial getSerial(final String port) {
        return new ArduinoSerial(port, baudRate);
    }

    void clearInputStream() {
        InputStream inputStream = getInputStream();
        try {
            int availableBytes = inputStream.available();
            if (availableBytes > 0) {
                inputStream.read(new byte[availableBytes]);
            }
        } catch (IOException e) {
            // Do nothing
        }
    }

    Optional<String> readWithTimeout(final int numberOfExpectedBytes) {
        final Future<String> serialInputString = Async.execute(() -> performReading(numberOfExpectedBytes));
        try {
            return Optional.of(serialInputString.get(5, TimeUnit.SECONDS));
        } catch (InterruptedException | TimeoutException | ExecutionException ex) {
            return Optional.empty();
        } finally {
            serialInputString.cancel(true);
        }
    }

    private String performReading(final int numberOfExpectedBytes)
            throws IOException {

        InputStream inputStream = this.getInputStream();
        while (inputStream.available() < numberOfExpectedBytes) {
            //Wait for available bytes
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return "";
            }
        }
        byte[] serialInputBytes = new byte[numberOfExpectedBytes];
        inputStream.read(serialInputBytes);
        return new String(serialInputBytes);
    }

    Optional<String> readLine() {

        final Future<String> serialInputString = Async.execute(() -> doReadLine());
        try {
            return Optional.of(serialInputString.get(5, TimeUnit.SECONDS));
        } catch (InterruptedException | TimeoutException | ExecutionException ex) {
            return Optional.empty();
        } finally {
            serialInputString.cancel(true);
        }
    }

    private String doReadLine() {
        String line = "";
        while (!line.endsWith("\r\n")) {
            line += readAvailable();
        }
        return line.replace("\r\n", "");
    }

    private String readAvailable() {
        try (InputStream inputStream = this.getInputStream()) {
            byte[] serialInputBytes = new byte[inputStream.available()];
            inputStream.read(serialInputBytes);
            return new String(serialInputBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
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

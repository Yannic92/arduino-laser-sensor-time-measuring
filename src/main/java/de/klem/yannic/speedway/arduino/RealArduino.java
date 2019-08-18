package de.klem.yannic.speedway.arduino;

import de.klem.yannic.speedway.arduino.event.ConnectivityEvent;
import de.klem.yannic.speedway.time.measure.LapTickHandler;
import de.klem.yannic.speedway.utils.async.AbstractObservable;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;
import java.util.TooManyListenersException;
import java.util.logging.Logger;

class RealArduino extends AbstractObservable<ConnectivityEvent> implements Arduino {

    private static final RealArduino INSTANCE = new RealArduino();
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private ArduinoSerial serial;
    private LaserSensorEventListener laserSensorEventListener;

    private boolean connected = false;

    private RealArduino() {
    }

    static Arduino getInstance() {
        return INSTANCE;
    }

    public boolean connect() {

        emit(ConnectivityEvent.CONNECTING(this));

        if (isConnected()) {
            return true;
        }

        List<String> availablePorts = ArduinoSerial.getAvailablePorts();
        for (String comPort : availablePorts) {
            connected = performConnect(comPort);
            if (connected) {

                try {
                    laserSensorEventListener = new LaserSensorEventListener(this.serial);
                    this.serial.addEventListener(laserSensorEventListener);
                } catch (TooManyListenersException e) {
                    throw new TooManyListenersRuntimeException(e);
                }

                emit(ConnectivityEvent.CONNECTED(this));
                return true;
            }
        }
        emit(ConnectivityEvent.DISCONNECTED(this));
        return false;


    }

    @Override
    public boolean isConnected() {
        return this.connected && this.serial != null && this.serial.isConnected();
    }

    @Override
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
        logger.info("Performing connect.");

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

        String waitingForHandshake = serial.readWithTimeout(23).orElse("");

        if (!"Waiting for Handshake\r\n".equals(waitingForHandshake)) {
            return false;
        }

        serial.write("Arduino\n");

        String uno = serial.readWithTimeout(5).orElse("");

        if (!"Uno\r\n".equals(uno)) {
            return false;
        }

        serial.write("Go\n");

        logger.info("Completed Handshake successfully.");
        return true;
    }

    @Override
    public void onLapTick(final LapTickHandler lapTickHandler) {
        this.laserSensorEventListener.setLapTickHandler(lapTickHandler);
    }

    @Override
    public void removeLapTickHandler() {
        this.laserSensorEventListener.setLapTickHandler(null);
    }
}

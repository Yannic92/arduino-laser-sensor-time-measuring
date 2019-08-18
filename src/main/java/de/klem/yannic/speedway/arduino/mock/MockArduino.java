package de.klem.yannic.speedway.arduino.mock;

import de.klem.yannic.speedway.arduino.Arduino;
import de.klem.yannic.speedway.arduino.event.ConnectivityEvent;
import de.klem.yannic.speedway.time.measure.LapTickHandler;
import de.klem.yannic.speedway.utils.async.AbstractObservable;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.logging.Logger;

public class MockArduino extends AbstractObservable<ConnectivityEvent> implements Arduino {

    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    static final MockArduino INSTANCE = new MockArduino();
    private LapTickHandler lapTickHandler;
    private boolean connected;
    private MockArduinoStage stage;

    private MockArduino() {
        connected = false;
        try {
            stage = new MockArduinoStage();
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }
    }

    public static Arduino getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public boolean connect() {
        emit(ConnectivityEvent.CONNECTING(this));
        connected = true;
        emit(ConnectivityEvent.CONNECTED(this));
        return connected;
    }

    @Override
    public void disconnect() {
        emit(ConnectivityEvent.DISCONNECTED(this));
    }

    @Override
    public void onLapTick(final LapTickHandler lapTickHandler) {
        this.lapTickHandler = lapTickHandler;
    }

    @Override
    public void removeLapTickHandler() {
        this.lapTickHandler = null;
    }

    void simulateTick() {
        if (lapTickHandler != null) {
            lapTickHandler.tick(Instant.now().toEpochMilli());
        }
    }
}

package de.klem.yannic.speedway.arduino;

import de.klem.yannic.speedway.measure.LapTickHandler;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.TooManyListenersException;
import java.util.logging.Logger;

public class ConnectedArduino {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static final Long secondsToLock = 5L;
    private final ArduinoSerial serial;
    private Instant lastTick;

    ConnectedArduino(ArduinoSerial serial) {
        this.serial = serial;
    }

    public boolean isConnected() {
        return this.serial.isConnected();
    }

    public void disconnect() {
        logger.info("Disconnect from Arduino.");
        this.serial.disconnect();
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

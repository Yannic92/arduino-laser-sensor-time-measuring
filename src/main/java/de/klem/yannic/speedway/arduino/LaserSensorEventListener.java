package de.klem.yannic.speedway.arduino;

import de.klem.yannic.speedway.time.measure.LapTickHandler;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.time.Instant;

public class LaserSensorEventListener implements SerialPortEventListener {

    private Instant lastTick;
    private static final Long secondsToLock = 5L;
    private LapTickHandler lapTickHandler;
    private final ArduinoSerial serial;

    LaserSensorEventListener(final ArduinoSerial serial) {
        this.serial = serial;
    }


    @Override
    public synchronized void serialEvent(SerialPortEvent serialPortEvent) {
        Instant now = Instant.now();
        if (lapTickHandler == null) {
            return;
        }

        if (lastTick == null || lastTick.isBefore(now.minusSeconds(secondsToLock))) {
            lastTick = now;
            lapTickHandler.tick(lastTick);
        }
        this.serial.clearInputStream();
    }

    synchronized void setLapTickHandler(final LapTickHandler lapTickHandler) {
        this.lapTickHandler = lapTickHandler;
        lastTick = null;
    }
}

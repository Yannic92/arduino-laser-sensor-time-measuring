package de.klem.yannic.speedway.arduino;

import de.klem.yannic.speedway.time.measure.LapTickHandler;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class LaserSensorEventListener implements SerialPortEventListener {

    private LapTickHandler lapTickHandler;
    private final ArduinoSerial serial;

    LaserSensorEventListener(final ArduinoSerial serial) {
        this.serial = serial;
    }


    @Override
    public synchronized void serialEvent(SerialPortEvent serialPortEvent) {
        if (lapTickHandler == null) {
            return;
        }

        serial.readLine().ifPresent(timeInMs -> {
            lapTickHandler.tick(Long.parseUnsignedLong(timeInMs));
        });
    }

    synchronized void setLapTickHandler(final LapTickHandler lapTickHandler) {
        this.lapTickHandler = lapTickHandler;
    }
}

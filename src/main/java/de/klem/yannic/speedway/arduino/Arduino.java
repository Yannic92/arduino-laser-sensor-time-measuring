package de.klem.yannic.speedway.arduino;

import de.klem.yannic.speedway.arduino.event.ConnectivityEvent;
import de.klem.yannic.speedway.time.measure.LapTickHandler;
import de.klem.yannic.speedway.utils.async.Observable;

public interface Arduino extends Observable<ConnectivityEvent> {
    boolean isConnected();

    boolean connect();

    void disconnect();

    void onLapTick(LapTickHandler lapTickHandler);

    void removeLapTickHandler();

    static Arduino getSingletonInstance() {
        return ArduinoFactory.getSingletonInstance();
    }
}

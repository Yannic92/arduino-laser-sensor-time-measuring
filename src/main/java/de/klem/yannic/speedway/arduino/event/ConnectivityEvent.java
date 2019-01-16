package de.klem.yannic.speedway.arduino.event;

import de.klem.yannic.speedway.arduino.Arduino;
import javafx.event.Event;
import javafx.event.EventType;

public abstract class ConnectivityEvent extends Event {

    private final Arduino source;

    private static final String CONNECTED_EVENT_NAME = "CONNECTED";
    private static final String DISCONNECTED_EVENT_NAME = "DISCONNECTED";
    private static final String CONNECTING_EVENT_NAME = "CONNECTING";

    public static final EventType<ConnectivityEvent> CONNECTED_TYPE = new EventType<>(CONNECTED_EVENT_NAME);
    public static final EventType<ConnectivityEvent> DISCONNECTED_TYPE = new EventType<>(DISCONNECTED_EVENT_NAME);
    public static final EventType<ConnectivityEvent> CONNECTING_TYPE = new EventType<>(CONNECTING_EVENT_NAME);

    public static ConnectedEvent CONNECTED(final Arduino connectedArduino) {
        return new ConnectedEvent(connectedArduino);
    }

    public static ConnectingEvent CONNECTING(final Arduino connectingArduino) {
        return new ConnectingEvent(connectingArduino);
    }

    public static DisconnectedEvent DISCONNECTED(final Arduino disconnectedArduino) {
        return new DisconnectedEvent(disconnectedArduino);
    }

    private ConnectivityEvent(EventType<? extends Event> eventType, Arduino source) {
        super(eventType);
        this.source = source;
    }

    @Override
    public Arduino getSource() {
        return this.source;
    }

    private static class ConnectedEvent extends ConnectivityEvent {
        private ConnectedEvent(Arduino connectedArduino) {
            super(CONNECTED_TYPE, connectedArduino);
        }
    }

    private static class DisconnectedEvent extends ConnectivityEvent {

        private DisconnectedEvent(Arduino disconnectedArduino) {
            super(DISCONNECTED_TYPE, disconnectedArduino);
        }
    }

    private static class ConnectingEvent extends ConnectivityEvent {

        private ConnectingEvent(Arduino connectingArduino) {
            super(CONNECTING_TYPE, connectingArduino);
        }
    }
}

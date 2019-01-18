package de.klem.yannic.speedway.utils.async;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

public interface Observable<T extends Event> {

    void addEventHandler(EventType<T> eventType, EventHandler<T> eventHandler);
}

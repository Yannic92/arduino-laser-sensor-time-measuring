package de.klem.yannic.speedway.utils.async;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractObservable<T extends Event> implements Observable<T> {

    private final Map<EventType<T>, List<EventHandler<T>>> eventHandlerMap = new ConcurrentHashMap<>();

    @Override
    public void addEventHandler(EventType<T> eventType, EventHandler<T> eventHandler) {
        if (eventHandlerMap.containsKey(eventType)) {
            eventHandlerMap.get(eventType).add(eventHandler);
        } else {
            ArrayList<EventHandler<T>> eventHandlers = new ArrayList<>();
            eventHandlers.add(eventHandler);
            eventHandlerMap.put(eventType, eventHandlers);
        }
    }

    protected void emit(T event) {
        eventHandlerMap.getOrDefault(event.getEventType(), new ArrayList<>())
                .forEach(eventHandler -> eventHandler.handle(event));
    }
}

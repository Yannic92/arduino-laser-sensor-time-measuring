package de.klem.yannic.speedway.arduino;

public class TooManyListenersRuntimeException extends RuntimeException {
    TooManyListenersRuntimeException(Throwable cause) {
        super(cause);
    }
}

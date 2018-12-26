package de.klem.yannic.speedway.arduino;

public class TooManyListenersRuntimeException extends RuntimeException {
    public TooManyListenersRuntimeException(Throwable cause) {
        super(cause);
    }
}

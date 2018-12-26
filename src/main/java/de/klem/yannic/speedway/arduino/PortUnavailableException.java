package de.klem.yannic.speedway.arduino;

import java.util.List;

class PortUnavailableException extends RuntimeException {

    PortUnavailableException(String port, List<String> availablePorts) {
        super(String.format("The port '%s' is not available. Available ports are: %s",
                port, availablePorts.toString()));
    }
}

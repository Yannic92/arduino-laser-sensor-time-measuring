package de.klem.yannic.speedway.arduino;

import de.klem.yannic.speedway.arduino.mock.MockArduino;

class ArduinoFactory {

    private static boolean mockEnabled = Boolean.valueOf(System.getProperty("mock.enabled"));
    private static final Arduino INSTANCE = getArduino();

    private static Arduino getArduino() {
        if (mockEnabled) {
            return MockArduino.getInstance();
        } else {
            return RealArduino.getInstance();
        }
    }

    static Arduino getSingletonInstance() {
        return INSTANCE;
    }
}

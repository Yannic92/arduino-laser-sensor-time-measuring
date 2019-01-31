package de.klem.yannic.speedway.time.measure;

@FunctionalInterface
public interface LapTickHandler {

    void tick(long timeOfTick);
}

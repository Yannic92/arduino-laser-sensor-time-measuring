package de.klem.yannic.speedway.time.measure;

import java.time.Instant;

@FunctionalInterface
public interface LapTickHandler {

    void tick(Instant timeOfTick);
}

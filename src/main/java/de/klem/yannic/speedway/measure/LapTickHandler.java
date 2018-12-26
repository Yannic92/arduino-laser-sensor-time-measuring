package de.klem.yannic.speedway.measure;

import java.time.Instant;

@FunctionalInterface
public interface LapTickHandler {

    void tick(Instant timeOfTick);
}

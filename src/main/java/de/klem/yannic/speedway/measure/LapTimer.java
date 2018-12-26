package de.klem.yannic.speedway.measure;

import java.time.Duration;
import java.time.Instant;

public class LapTimer implements LapTickHandler {

    private final LapFinishedHandler lapFinishedHandler;
    private Instant lastTick;

    public LapTimer(LapFinishedHandler lapFinishedHandler) {
        this.lapFinishedHandler = lapFinishedHandler;
    }

    @Override
    public void tick(Instant timeOfTick) {
        if (lastTick == null) {
            lastTick = timeOfTick;
        } else {
            Duration lapDuration = Duration.between(lastTick, timeOfTick);
            lapFinishedHandler.lapFinished(lapDuration);
        }
    }
}

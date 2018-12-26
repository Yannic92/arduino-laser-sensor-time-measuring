package de.klem.yannic.speedway.measure;

import java.time.Duration;

@FunctionalInterface
public interface LapFinishedHandler {

    void lapFinished(final Duration lapDuration);
}

package de.klem.yannic.speedway.time.measure;

import java.time.Duration;

@FunctionalInterface
public interface LapFinishedHandler {

    void lapFinished(final Duration lapDuration);
}

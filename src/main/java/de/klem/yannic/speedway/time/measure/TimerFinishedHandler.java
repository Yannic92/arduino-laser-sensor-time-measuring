package de.klem.yannic.speedway.time.measure;

import java.time.Duration;

@FunctionalInterface
public interface TimerFinishedHandler {

    void timerFinished(final Duration... lapDurations);
}

package de.klem.yannic.speedway.time.measure;

import de.klem.yannic.speedway.driver.Driver;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LapTimer extends AnimationTimer implements LapTickHandler {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private final TimerFinishedHandler timerFinishedHandler;
    private final Driver driver;
    private final List<Duration> lapTimes;
    private final Label timeLabel;
    private final Integer lapsToDrive = 2;
    private Integer lapCounter = 0;
    private long startTime;
    private Long lastTick;

    public LapTimer(final Driver driver, final Label timeLabel, final TimerFinishedHandler timerFinishedHandler) {
        this.driver = driver;
        this.timeLabel = timeLabel;
        this.timerFinishedHandler = timerFinishedHandler;
        this.lapTimes = new ArrayList<>();
    }

    @Override
    public void tick(long timeOfTick) {
        if (lastTick == null) {
            lastTick = timeOfTick;
            startTime = Instant.now().toEpochMilli();
            start();
        } else {
            handleFinishedLap(Duration.ofMillis(timeOfTick - lastTick));
            lastTick = timeOfTick;
        }
    }

    private void handleFinishedLap(final Duration duration) {
        this.lapTimes.add(duration);
        lapCounter++;
        if (lapCounter >= lapsToDrive) {
            stop();
            setTimeLabelToSumOfDurations();
        }
        String message = String.format("%s finished lap %d of %d in %f seconds",
                this.driver.getFirstName(), lapCounter, lapsToDrive, duration.toMillis() / 1000.0);
        logger.info(message);
    }


    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        timerFinishedHandler.timerFinished(lapTimes.toArray(new Duration[0]));
    }

    private void setTimeLabelToSumOfDurations() {
        Platform.runLater(() -> {
            Duration sum = Duration.ZERO;
            for (Duration lapTime : lapTimes) {
                sum = sum.plus(lapTime);
            }
            timeLabel.setText(DurationFormatUtils.formatDuration(sum.toMillis(), "mm:ss:SS"));
        });
    }

    @Override
    public void handle(long now) {
        long elapsedTimeSinceStartInMillis = System.currentTimeMillis() - startTime;
        timeLabel.setText(DurationFormatUtils.formatDuration(elapsedTimeSinceStartInMillis, "mm:ss:SS"));
    }
}

package de.klem.yannic.speedway.time.timetable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Round {
    private final List<Duration> lapTimes;
    private final int pylonCount;
    private final int misses;

    private Round(final List<Duration> lapTimes, final int pylonCount, final int misses) {
        this.lapTimes = new ArrayList<>(lapTimes);
        this.pylonCount = pylonCount;
        this.misses = misses;
    }

    public static Round start() {
        return new Round(new ArrayList<>(), 0, 0);
    }

    public static Round fromCSV(String csv) {
        String[] fields = csv.split(",");
        final int pylonCount = Integer.parseInt(fields[0]);
        final int misses = Integer.parseInt(fields[1]);
        final List<Duration> lapTimes = new ArrayList<>();
        for(int index = 2; index < fields.length; index++) {
            lapTimes.add(Duration.ofMillis(Long.parseUnsignedLong(fields[index])));
        }
        return new Round(lapTimes, pylonCount, misses);
    }

    public Round addLapTime(final Duration lapTime) {
        final List<Duration> lapTimes = new ArrayList<>(this.lapTimes);
        lapTimes.add(lapTime);
        return new Round(lapTimes, pylonCount, misses);
    }

    public Round setPylonCount(final int pylonCount) {
        return new Round(lapTimes, pylonCount, misses);
    }

    public Round setMisses(final int misses) {
        return new Round(lapTimes, pylonCount, misses);
    }

    public String toCSV() {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(pylonCount);
        stringBuilder.append(",");
        stringBuilder.append(misses);
        stringBuilder.append(",");

        for(Duration lapTime : lapTimes) {
            stringBuilder.append(lapTime.toMillis());
            stringBuilder.append(",");
        }

        return stringBuilder.toString();
    }
}

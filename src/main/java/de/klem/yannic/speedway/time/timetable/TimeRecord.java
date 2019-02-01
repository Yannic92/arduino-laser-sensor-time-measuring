package de.klem.yannic.speedway.time.timetable;

import de.klem.yannic.speedway.driver.Driver;

import java.util.*;

public class TimeRecord implements Map.Entry<Driver, List<Round>> {
    private final Driver driver;
    private List<Round> rounds;



    private TimeRecord(Driver driver, List<Round> rounds) {
        this.driver = driver;
        this.rounds = rounds;
    }

    public static TimeRecord forDriver(final Driver driver) {
        return new TimeRecord(driver, new ArrayList<>());
    }

    public static TimeRecord fromCSV(String csv) {
        final String[] split = csv.split(";");
        final String driverCSV = split[0];
        final Driver driver = Driver.fromCSV(driverCSV);
        final List<Round> rounds = new ArrayList<>();
        for(int index = 1; index < split.length; index++) {
            rounds.add(Round.fromCSV(split[index]));
        }

        return new TimeRecord(driver, rounds);
    }

    @Override
    public Driver getKey() {
        return driver;
    }

    @Override
    public List<Round> getValue() {
        return new ArrayList<>(rounds);
    }

    @Override
    public List<Round> setValue(List<Round> rounds) {
        final List<Round> oldRounds = this.rounds;
        this.rounds = new ArrayList<>(rounds);
        return oldRounds;
    }
}

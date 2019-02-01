package de.klem.yannic.speedway.time.timetable;

import de.klem.yannic.speedway.driver.Driver;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TimeRecords extends HashMap<Driver, List<Round>> {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static final String DRIVERS_FILE_NAME = "./../time-records.db";
    private static final File TIME_RECORDS_FILE_LOCATION;
    private static final File driversFile;

    private static final TimeRecords instance;

    static {
        try {
            TIME_RECORDS_FILE_LOCATION = new File(TimeRecords.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Could not load drivers file.", e);
        }
        driversFile = new File(TIME_RECORDS_FILE_LOCATION, DRIVERS_FILE_NAME);
        if (!driversFile.exists()) {
            try {
                logger.info("Creating new drivers file: " + driversFile.getAbsolutePath());
                driversFile.createNewFile();
            } catch (IOException e) {
                throw new IllegalStateException("Could not create drivers file.", e);
            }
        }
        instance = loadFromFile();
    }

    private final ObservableMap<Driver, List<Round>> timeRecords = FXCollections.observableHashMap();

    private TimeRecords(final Map<Driver, List<Round>> initialTimeRecords) {
        this.timeRecords.putAll(initialTimeRecords);
        this.timeRecords.addListener((MapChangeListener<Driver, List<Round>>) c -> writeToFile(driversFile));
    }

    private static TimeRecords loadFromFile() {
        return new TimeRecords(readFromFile(driversFile));
    }

    private static Map<Driver, List<Round>> readFromFile(final File file) {
        final Map<Driver, List<Round>> timeRecords = new HashMap<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                TimeRecord timeRecord = TimeRecord.fromCSV(line);
                timeRecords.put(timeRecord.getKey(), timeRecord.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return timeRecords;
    }

    private void writeToFile(final File file) {

        try {
            final PrintWriter printWriter = new PrintWriter(file);
            timeRecords.forEach((driver, rounds) -> {
                StringBuilder stringBuilder = new StringBuilder(driver.toCSV());
                stringBuilder.append(";");
                for(Round round : rounds) {
                    stringBuilder.append(round.toCSV());
                }
                stringBuilder.append("\n");
                printWriter.write(stringBuilder.toString());
            });
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not write drivers to file");
        }
    }

    static TimeRecords getInstance() {
        return instance;
    }

}

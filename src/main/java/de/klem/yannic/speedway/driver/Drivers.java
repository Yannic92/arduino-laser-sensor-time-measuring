package de.klem.yannic.speedway.driver;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Drivers {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static final String DRIVERS_FILE_NAME = "./../drivers.csv";
    private static final File DRIVERS_FILE_LOCATION;
    private static final File driversFile;
    private static final Drivers instance;

    static {
        try {
            DRIVERS_FILE_LOCATION = new File(Drivers.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Could not load drivers file.", e);
        }
        driversFile = new File(DRIVERS_FILE_LOCATION, DRIVERS_FILE_NAME);
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


    private final ObservableList<Driver> drivers = FXCollections.observableArrayList();

    private Drivers() {
    }

    private Drivers(final List<Driver> initialDrivers) {
        this.drivers.addAll(initialDrivers);
        this.drivers.addListener((ListChangeListener<Driver>) c -> writeToFile());
    }

    private static Drivers loadFromFile() {
        return new Drivers(readFromFile());
    }

    private static List<Driver> readFromFile() {
        final List<Driver> drivers = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(driversFile))) {
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                drivers.add(Driver.fromCSV(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    private void writeToFile() {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(driversFile);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not write drivers to file");
        }
        for (Driver driver : drivers) {
            printWriter.write(driver.toCSV() + "\n");
        }
        printWriter.flush();
        printWriter.close();
    }

    public static Drivers getInstance() {
        return instance;
    }

    public ObservableList<Driver> getDriversList() {
        return this.drivers;
    }
}

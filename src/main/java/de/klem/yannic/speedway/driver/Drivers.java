package de.klem.yannic.speedway.driver;

import de.klem.yannic.speedway.time.timetable.TimeRecord;
import de.klem.yannic.speedway.time.timetable.TimeRecords;
import javafx.collections.*;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

class Drivers {

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

    private final ObservableMap<Driver, TimeRecord> timeRecords = FXCollections.observableHashMap();

    private Drivers(final Map<Driver, TimeRecord> initialTimeRecords) {
        this.timeRecords.putAll(initialTimeRecords);
        this.timeRecords.addListener((MapChangeListener<Driver, TimeRecord>) c -> writeToFile(driversFile));
    }

    private static Drivers loadFromFile() {
        return new Drivers(readFromFile(driversFile));
    }

    private static Map<Driver, TimeRecord> readFromFile(final File file) {
        final Map<Driver, TimeRecord> drivers = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                String[] splitedLine = line.split(";");
                Driver.fromCSV(splitedLine[0]);
                TimeRecord.fromCSV(splitedLine[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    private void writeToFile(final File file) {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not write drivers to file");
        }
        for (Driver driver : drivers) {
            printWriter.write(driver.toCSV() + "\n");
        }
        printWriter.flush();
        printWriter.close();
    }

    static Drivers getInstance() {
        return instance;
    }

    ObservableList<Driver> getDriversList() {
        return FXCollections.unmodifiableObservableList(this.drivers);
    }

    boolean remove(final Driver driverToRemove) {
        return this.drivers.remove(driverToRemove);
    }

    boolean add(final Driver driver) {
        return this.drivers.add(driver);
    }

    boolean addAll(final Collection<Driver> drivers) {
        return this.drivers.addAll(drivers);
    }

    void clear() {
        this.drivers.clear();
    }

    void load(File fileToImport) {
        List<Driver> drivers = readFromFile(fileToImport);
        clear();
        addAll(drivers);
    }

    void save(File file) {
        writeToFile(file);
    }
}

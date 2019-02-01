package de.klem.yannic.speedway.driver;

import java.util.Arrays;

public class Driver {

    private static final Integer FIRST_NAME_CSV_INDEX = 0;
    private static final Integer LAST_NAME_CSV_INDEX = 1;
    private static final Integer CLUB_CSV_INDEX = 2;
    private static final Integer START_NUMBER_CSV_INDEX = 3;
    private static final Integer CLASS_CSV_INDEX = 4;

    private static final Integer NUMBER_OF_CSV_FIELDS = 5;

    private final String firstName;
    private final String lastName;
    private final String club;
    private final String startNumber;
    private final String driverClass;

    Driver(String firstName, String lastName, String club, String startNumber, String driverClass) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.club = club;
        this.startNumber = startNumber;
        this.driverClass = driverClass;
    }

    public static Driver fromCSV(final String csv) {
        final String[] driverFields = csv.split(",");

        if (driverFields.length < NUMBER_OF_CSV_FIELDS) {
            throw new IllegalStateException(String.format("Could not create Driver from CSV: '%s'. " +
                            "Invalid number of fields in CSV. Required '%d' found '%d'.",
                    csv, driverFields.length, NUMBER_OF_CSV_FIELDS));
        }

        return new Driver(
                driverFields[Driver.FIRST_NAME_CSV_INDEX],
                driverFields[Driver.LAST_NAME_CSV_INDEX],
                driverFields[Driver.CLUB_CSV_INDEX],
                driverFields[Driver.START_NUMBER_CSV_INDEX],
                driverFields[Driver.CLASS_CSV_INDEX]
        );
    }

    public String toCSV() {
        final String[] driverFields = new String[5];
        driverFields[FIRST_NAME_CSV_INDEX] = firstName;
        driverFields[LAST_NAME_CSV_INDEX] = lastName;
        driverFields[CLUB_CSV_INDEX] = club;
        driverFields[START_NUMBER_CSV_INDEX] = startNumber;
        driverFields[CLASS_CSV_INDEX] = driverClass;
        return String.join(",", driverFields);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getClub() {
        return club;
    }

    public String getStartNumber() {
        return startNumber;
    }

    public String getDriverClass() {
        return driverClass;
    }

    boolean anyFieldMatches(String filterText) {
        return this.driverClass.contains(filterText) || this.firstName.contains(filterText) ||
                this.lastName.contains(filterText) || this.startNumber.contains(filterText) ||
                this.club.contains(filterText);
    }
}

package de.klem.yannic.speedway.driver;

public class Driver {

    private final String firstName;
    private final String lastName;
    private final String club;
    private final String startNumber;
    private final String driverClass;

    public Driver(String firstName, String lastName, String club, String startNumber, String driverClass) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.club = club;
        this.startNumber = startNumber;
        this.driverClass = driverClass;
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
}

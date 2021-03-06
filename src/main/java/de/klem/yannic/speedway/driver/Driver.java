package de.klem.yannic.speedway.driver;

import org.eclipse.ditto.json.JsonFieldDefinition;
import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.json.JsonObjectBuilder;

import java.util.Optional;

public class Driver {

    private static final JsonFieldDefinition<String> FIRST_NAME = JsonFieldDefinition.ofString("firstName");
    private static final JsonFieldDefinition<String> LAST_NAME = JsonFieldDefinition.ofString("lastName");
    private static final JsonFieldDefinition<String> CLUB = JsonFieldDefinition.ofString("club");
    private static final JsonFieldDefinition<String> START_NUMBER = JsonFieldDefinition.ofString("startNumber");
    private static final JsonFieldDefinition<String> DRIVER_CLASS = JsonFieldDefinition.ofString("driverClass");
    private static final JsonFieldDefinition<JsonObject> RUN_1 = JsonFieldDefinition.ofJsonObject("run1");
    private static final JsonFieldDefinition<JsonObject> RUN_2 = JsonFieldDefinition.ofJsonObject("run2");

    private final String firstName;
    private final String lastName;
    private final String club;
    private final String startNumber;
    private final String driverClass;
    private final Run run1;
    private final Run run2;

    Driver(final String firstName, final String lastName, final String club, final String startNumber,
           final String driverClass) {

        this(firstName, lastName, club, startNumber, driverClass, null, null);
    }

    Driver(final String firstName, final String lastName, final String club, final String startNumber,
           final String driverClass, final Run run1, final Run run2) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.club = club;
        this.startNumber = startNumber;
        this.driverClass = driverClass;
        this.run1 = run1;
        this.run2 = run2;
    }

    JsonObject toJson() {
        JsonObjectBuilder driverJsonBuilder = JsonObject.newBuilder()
                .set(FIRST_NAME, firstName)
                .set(LAST_NAME, lastName)
                .set(CLUB, club)
                .set(START_NUMBER, startNumber)
                .set(DRIVER_CLASS, driverClass);

        if (run1 != null) {
            driverJsonBuilder.set(RUN_1, run1.toJson());
        }

        if (run2 != null) {
            driverJsonBuilder.set(RUN_2, run2.toJson());
        }

        return driverJsonBuilder.build();
    }

    static Driver fromJson(final JsonObject jsonObject) {
        final String firstName = jsonObject.getValueOrThrow(FIRST_NAME);
        final String lastName = jsonObject.getValueOrThrow(LAST_NAME);
        final String club = jsonObject.getValueOrThrow(CLUB);
        final String startNumber = jsonObject.getValueOrThrow(START_NUMBER);
        final String driverClass = jsonObject.getValueOrThrow(DRIVER_CLASS);
        final Run run1 = jsonObject.getValue(RUN_1)
                .map(Run::fromJson)
                .orElse(null);
        final Run run2 = jsonObject.getValue(RUN_2)
                .map(Run::fromJson)
                .orElse(null);
        return new Driver(firstName, lastName, club, startNumber, driverClass, run1, run2);
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

    public Optional<Run> getRun1() {
        return Optional.ofNullable(run1);
    }

    public Optional<Run> getRun2() {
        return Optional.ofNullable(run2);
    }

    boolean anyFieldMatches(String filterText) {
        return this.driverClass.contains(filterText) || this.firstName.contains(filterText) ||
                this.lastName.contains(filterText) || this.startNumber.contains(filterText) ||
                this.club.contains(filterText);
    }

    public Driver setRun1(Run run1) {
        return new Driver(firstName, lastName, club, startNumber, driverClass, run1, run2);
    }

    public Driver setRun2(Run run2) {
        return new Driver(firstName, lastName, club, startNumber, driverClass, run1, run2);
    }
}

package de.klem.yannic.speedway.driver;

import org.eclipse.ditto.json.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Run {

    private static final JsonFieldDefinition<JsonArray> LAPS = JsonFieldDefinition.ofJsonArray("laps");
    private static final JsonFieldDefinition<Integer> PYLONS = JsonFieldDefinition.ofInt("pylons");
    private static final JsonFieldDefinition<Integer> CHALLENGES = JsonFieldDefinition.ofInt("challenges");

    private final List<Duration> laps;
    private final int pylons;
    private final int challenges;


    private Run(final List<Duration> laps, final int pylons, final int challenges) {
        this.laps = Collections.unmodifiableList(new ArrayList<>(laps));
        this.pylons = pylons;
        this.challenges = challenges;
    }

    static Run newInstance() {
        return new Run(Collections.emptyList(), 0, 0);
    }

    static Run fromJson(final JsonObject jsonObject) {
        final List<Duration> laps = jsonObject.getValueOrThrow(LAPS).stream()
                .map(JsonValue::asLong)
                .map(Duration::ofNanos)
                .collect(Collectors.toList());
        final Integer pylons = jsonObject.getValueOrThrow(PYLONS);
        final Integer challenges = jsonObject.getValueOrThrow(CHALLENGES);
        return new Run(laps, pylons, challenges);
    }

    JsonObject toJson() {
        final JsonArray jsonLaps = laps.stream()
                .map(Duration::toNanos)
                .map(JsonValue::of)
                .collect(JsonCollectors.valuesToArray());

        return JsonObject.newBuilder()
                .set(LAPS, jsonLaps)
                .set(PYLONS, pylons)
                .set(CHALLENGES, challenges)
                .build();
    }

    public Run addLapTime(final Duration lapTime) {
        final List<Duration> newLaps = new ArrayList<>(laps);
        newLaps.add(lapTime);
        return new Run(newLaps, pylons, challenges);
    }

    public Run setPylons(final int pylons) {
        return new Run(laps, pylons, challenges);
    }

    public Run setChallenges(final int challenges) {
        return new Run(laps, pylons, challenges);
    }

    public List<Duration> getLaps() {
        return laps;
    }

    public int getPylons() {
        return pylons;
    }

    public int getChallenges() {
        return challenges;
    }

}

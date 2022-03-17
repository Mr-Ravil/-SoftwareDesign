package statistic;

import clock.Clock;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RpmEventsStatistic implements EventsStatistic {
    private static final TimeUnit TIME_OF_RELEVANCE = TimeUnit.HOURS;
    private static final long TIME_OF_RELEVANCE_IN_SECONDS = TIME_OF_RELEVANCE.toSeconds(1L);
    private static final long TIME_OF_RELEVANCE_IN_MINUTES = TIME_OF_RELEVANCE.toMinutes(1L);


    private final Clock clock;
    private final Map<String, List<Instant>> eventsHistory;

    public RpmEventsStatistic(Clock clock) {
        this.clock = clock;
        this.eventsHistory = new HashMap<>();
    }

    private void clean(Instant now) {
        Instant lastRelevantTime = now.minusSeconds(TIME_OF_RELEVANCE_IN_SECONDS).plusSeconds(1);
        eventsHistory.forEach((name, events) -> events.removeIf(event -> event.isBefore(lastRelevantTime)));
        eventsHistory.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    @Override
    public void incEvent(String name) {
        Instant now = clock.now();
        eventsHistory.putIfAbsent(name, new ArrayList<>());
        eventsHistory.get(name).add(now);
        clean(now);
    }

    private double getEventFixedStatisticByName(String name) {
        return eventsHistory.getOrDefault(name, List.of()).size() / (double) TIME_OF_RELEVANCE_IN_MINUTES;
    }

    @Override
    public double getEventStatisticByName(String name) {
        Instant now = clock.now();
        clean(now);
        return getEventFixedStatisticByName(name);
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        Instant now = clock.now();
        clean(now);
        return eventsHistory.keySet().stream()
                .collect(Collectors.toMap(name -> name, this::getEventFixedStatisticByName));
    }

    @Override
    public void printStatistic() {
        Instant now = clock.now();
        clean(now);
        Map<String, Double> allEventStatistic = getAllEventStatistic();
        allEventStatistic.forEach((name, rpm) -> System.out.println("Name: " + name + ", rpm: " + rpm));
    }
}

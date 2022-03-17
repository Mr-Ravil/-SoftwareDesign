import clock.SettableClock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import statistic.EventsStatistic;
import statistic.RpmEventsStatistic;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EventsStatisticTest {
    private static final TimeUnit TIME_OF_RELEVANCE = TimeUnit.HOURS;
    private static final long TIME_OF_RELEVANCE_IN_SECONDS = TIME_OF_RELEVANCE.toSeconds(1L);
    private static final long TIME_OF_RELEVANCE_IN_MINUTES = TIME_OF_RELEVANCE.toMinutes(1L);

    private final String eventName = "event";
    private final List<String> eventsNames = List.of("event1", "event2", "event3");
    private final int times = 3;

    private SettableClock clock;
    private EventsStatistic eventsStatistic;

    @Before
    public void setUp() {
        clock = new SettableClock();
        eventsStatistic = new RpmEventsStatistic(clock);
        clock.setNow(Instant.now());
    }


    @Test
    public void testStatisticByNameEmpty() {
        Assert.assertEquals(0, eventsStatistic.getEventStatisticByName(eventName), 0.0);
    }

    @Test
    public void testStatisticByNameWithSingleInc() {
        eventsStatistic.incEvent(eventName);

        Assert.assertEquals((double) 1 / TIME_OF_RELEVANCE_IN_MINUTES,
                eventsStatistic.getEventStatisticByName(eventName), 0.0);
    }

    @Test
    public void testStatisticByNameWithSeveralInc() {
        for (int i = 0; i < times; i++) {
            eventsStatistic.incEvent(eventName);
        }

        Assert.assertEquals((double) times / TIME_OF_RELEVANCE_IN_MINUTES,
                eventsStatistic.getEventStatisticByName(eventName), 0.0);
    }

    @Test
    public void testStatisticByNameWithDifferentTime() {
        for (int i = 0; i < times; i++) {
            eventsStatistic.incEvent(eventName);
            clock.setNow(clock.getNow().plusSeconds(1));
        }

        Assert.assertEquals((double) times / TIME_OF_RELEVANCE_IN_MINUTES,
                eventsStatistic.getEventStatisticByName(eventName), 0.0);
    }

    @Test
    public void testAllStatisticEmpty() {
        Assert.assertEquals(Map.of(), eventsStatistic.getAllEventStatistic());
    }

    @Test
    public void testAllStatisticSimple() {
        for (String eventName : eventsNames) {
            for (int i = 0; i < times; i++) {
                eventsStatistic.incEvent(eventName);
            }
        }

        Map<String, Double> actual = new HashMap<>();
        for (String eventName : eventsNames) {
            actual.put(eventName, (double) times / TIME_OF_RELEVANCE_IN_MINUTES);
        }

        Assert.assertEquals(actual, eventsStatistic.getAllEventStatistic());
    }

    @Test
    public void testAllStatisticWithDifferentTime() {
        for (String eventName : eventsNames) {
            for (int i = 0; i < times; i++) {
                eventsStatistic.incEvent(eventName);
                clock.setNow(clock.getNow().plusSeconds(1));
            }
        }

        Map<String, Double> actual = new HashMap<>();
        for (String eventName : eventsNames) {
            actual.put(eventName, (double) times / TIME_OF_RELEVANCE_IN_MINUTES);
        }

        Assert.assertEquals(actual, eventsStatistic.getAllEventStatistic());
    }

    @Test
    public void testOutOfTimeStatisticByNameSimple() {
        eventsStatistic.incEvent(eventName);
        clock.setNow(clock.getNow().plusSeconds(TIME_OF_RELEVANCE_IN_SECONDS));

        Assert.assertEquals(0, eventsStatistic.getEventStatisticByName(eventName), 0.0);
    }

    @Test
    public void testOutOfTimeStatisticByNameSeveralInc() {
        long partOfTime = TIME_OF_RELEVANCE_IN_SECONDS / times;

        for (int i = 0; i < times; i++) {
            eventsStatistic.incEvent(eventName);
            clock.setNow(clock.getNow().plusSeconds(partOfTime));
        }
        clock.setNow(clock.getNow().plusSeconds(1));

        Assert.assertEquals((double) (times - 1) / TIME_OF_RELEVANCE_IN_MINUTES, eventsStatistic.getEventStatisticByName(eventName), 0.0);
    }

    @Test
    public void testOutOfTimeAllStatisticSimple() {
        long partOfTime = TIME_OF_RELEVANCE_IN_SECONDS / times;

        for (int i = 0; i < times; i++) {
            for (String eventName : eventsNames) {
                eventsStatistic.incEvent(eventName);
            }
            clock.setNow(clock.getNow().plusSeconds(partOfTime));
        }
        clock.setNow(clock.getNow().plusSeconds(1));

        Map<String, Double> actual = new HashMap<>();
        for (String eventName : eventsNames) {
            actual.put(eventName, (double) (times - 1) / TIME_OF_RELEVANCE_IN_MINUTES);
        }

        Assert.assertEquals(actual, eventsStatistic.getAllEventStatistic());
    }

    @Test
    public void testOutOfTimeAllStatisticWithDifferentTime() {
        long partOfTime = TIME_OF_RELEVANCE_IN_SECONDS / times;

        for (int i = 0; i < times; i++) {
            for (String eventName : eventsNames) {
                eventsStatistic.incEvent(eventName);
                clock.setNow(clock.getNow().plusSeconds(1));
            }
            clock.setNow(clock.getNow().plusSeconds(partOfTime));
        }
        clock.setNow(clock.getNow().plusSeconds(1));

        Map<String, Double> actual = new HashMap<>();
        for (String eventName : eventsNames) {
            actual.put(eventName, (double) (times - 1) / TIME_OF_RELEVANCE_IN_MINUTES);
        }

        Assert.assertEquals(actual, eventsStatistic.getAllEventStatistic());
    }
}

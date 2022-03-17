import clock.NormalClock;
import statistic.EventsStatistic;
import statistic.RpmEventsStatistic;

import java.util.List;

public class EventsStatisticPrintTest {
    public static void main(String[] args) {
        EventsStatistic eventsStatistic = new RpmEventsStatistic(new NormalClock());

        List<String> eventsNames = List.of("event1", "event2", "event3");
        List<Integer> eventsTimes = List.of(3, 1, 2);
        int eventCount = eventsNames.size();

        for (int event = 0; event < eventCount; event++) {
            for (int i = 0; i < eventsTimes.get(event); i++) {
                eventsStatistic.incEvent(eventsNames.get(event));
            }
        }

        eventsStatistic.printStatistic();
    }
}

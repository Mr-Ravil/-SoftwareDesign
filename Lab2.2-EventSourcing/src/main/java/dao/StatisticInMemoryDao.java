package dao;

import events.EntryEvent;
import events.ExitEvent;
import model.Stats;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class StatisticInMemoryDao implements StatisticDao {

    private static final Duration DAY = Duration.ofDays(1);
    private final TreeMap<LocalDate, RawStats> dailyStats;
    private final Map<Integer, LocalDateTime> lastVisit;
    private final RawStats globalStats;

    public StatisticInMemoryDao() {
        dailyStats = new TreeMap<>();
        lastVisit = new HashMap<>();
        globalStats = new RawStats();
    }

    @Override
    public void addEntryEvent(EntryEvent event) {
        lastVisit.put(event.getAccountId(), event.getEntryTime());
    }

    @Override
    public void addExitEvent(ExitEvent event) {
        LocalDateTime entryTime = lastVisit.remove(event.getAccountId());
        Duration duration = Duration.between(entryTime, event.getExitTime());

        globalStats.incrementVisits();
        globalStats.incrementDuration(duration);

        if (entryTime.toLocalDate().isEqual(event.getExitTime().toLocalDate())) {
            RawStats dailyStat = dailyStats.putIfAbsent(entryTime.toLocalDate(), new RawStats(1, duration));
            if (dailyStat != null) {
                dailyStat.incrementVisits();
                dailyStat.incrementDuration(duration);
            }
        } else {

            for (LocalDate localDate = entryTime.toLocalDate().plusDays(1);
                 localDate.isBefore(event.getExitTime().toLocalDate());
                 localDate = localDate.plusDays(1)) {

                RawStats dailyStat = dailyStats.putIfAbsent(localDate, new RawStats(1, DAY));
                if (dailyStat != null) {
                    dailyStat.incrementVisits();
                    dailyStat.incrementDuration(DAY);
                }
            }

            Duration firstDayDuration = Duration.between(entryTime, entryTime.toLocalDate().plusDays(1).atStartOfDay());
            RawStats firstDayStats = dailyStats.putIfAbsent(
                    entryTime.toLocalDate(), new RawStats(1, firstDayDuration)
            );
            if (firstDayStats != null) {
                firstDayStats.incrementVisits();
                firstDayStats.incrementDuration(firstDayDuration);
            }

            Duration lastDayDuration = Duration.between(event.getExitTime().toLocalDate().atStartOfDay(), event.getExitTime());
            RawStats lastDayStats = dailyStats.putIfAbsent(
                    event.getExitTime().toLocalDate(), new RawStats(1, lastDayDuration)
            );
            if (lastDayStats != null) {
                lastDayStats.incrementVisits();
                lastDayStats.incrementDuration(lastDayDuration);
            }
        }
    }

    @Override
    public long getNumberOfVisits() {
        return globalStats.getNumberOfVisits() + lastVisit.size();
    }

    @Override
    public Duration getAverageDuration(LocalDateTime currentTime) {
        Duration duration = globalStats.getCommonDuration();

        for (LocalDateTime entryTime : lastVisit.values()) {
            duration = duration.plus(Duration.between(entryTime, currentTime));
        }

        return globalStats.getNumberOfVisits() == 0 ? Duration.ZERO : duration.dividedBy(globalStats.getNumberOfVisits());
    }

    @Override
    public List<Stats> getStats(LocalDate from, LocalDate to) {
        List<RawStats> rawStatsList = new ArrayList<>();

        for (LocalDate date = from; date.isBefore(to); date = date.plusDays(1)) {
            RawStats dailyStat = dailyStats.getOrDefault(date, new RawStats());
            rawStatsList.add(new RawStats(dailyStat));
        }

        for (LocalDateTime entryTime : lastVisit.values()) {
            int index = (int) Duration.between(from.atStartOfDay(), entryTime.toLocalDate().atStartOfDay()).toDays();

            RawStats firstDayStats = rawStatsList.get(index);
            firstDayStats.incrementVisits();
            firstDayStats.incrementDuration(
                    Duration.between(entryTime, entryTime.toLocalDate().plusDays(1).atStartOfDay())
            );

            for (int i = index + 1; i < rawStatsList.size(); i++) {
                rawStatsList.get(i).incrementVisits();
                rawStatsList.get(i).incrementDuration(DAY);
            }
        }

        return rawStatsList.stream().map(RawStats::convertToStats).toList();
    }

    private static class RawStats {
        private long numberOfVisits;
        private Duration commonDuration;

        public RawStats(RawStats rawStats) {
            this.numberOfVisits = rawStats.numberOfVisits;
            this.commonDuration = rawStats.commonDuration;
        }

        public RawStats() {
            this.numberOfVisits = 0;
            this.commonDuration = Duration.ZERO;
        }

        public RawStats(long numberOfVisits, Duration commonDuration) {
            this.numberOfVisits = numberOfVisits;
            this.commonDuration = commonDuration;
        }

        public RawStats(long numberOfVisits) {
            this.numberOfVisits = 0;
            this.commonDuration = Duration.ZERO;
        }

        public Stats convertToStats() {
            return new Stats(numberOfVisits, numberOfVisits == 0 ?
                    Duration.ZERO : commonDuration.dividedBy(numberOfVisits));
        }

        public void incrementVisits() {
            ++numberOfVisits;
        }

        public void incrementDuration(Duration duration) {
            commonDuration = commonDuration.plus(duration);
        }

        public long getNumberOfVisits() {
            return numberOfVisits;
        }

        public void setNumberOfVisits(long numberOfVisits) {
            this.numberOfVisits = numberOfVisits;
        }

        public Duration getCommonDuration() {
            return commonDuration;
        }

        public void setCommonDuration(Duration commonDuration) {
            this.commonDuration = commonDuration;
        }
    }


}

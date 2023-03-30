package dao;

import events.EntryEvent;
import events.ExitEvent;
import model.Stats;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StatisticDao {
    void addEntryEvent(EntryEvent event);

    void addExitEvent(ExitEvent event);

    long getNumberOfVisits();

    Duration getAverageDuration(LocalDateTime currentTime);

    List<Stats> getStats(LocalDate from, LocalDate to);
}

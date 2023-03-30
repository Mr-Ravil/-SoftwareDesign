package storage;

import dao.StatisticDao;
import events.EntryEvent;
import events.ExitEvent;
import model.Stats;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CommandApplication {
    private StatisticDao statisticDao;

    public CommandApplication(StatisticDao statisticDao) {
        this.statisticDao = statisticDao;
    }

    public EntryEvent enter(int accountId, LocalDateTime entryTime) {
        EntryEvent entryEvent = new EntryEvent(accountId, entryTime);
        statisticDao.addEntryEvent(entryEvent);
        return entryEvent;
    }

    public ExitEvent exit(int accountId, LocalDateTime exitTime) {
        ExitEvent exitEvent = new ExitEvent(accountId, exitTime);
        statisticDao.addExitEvent(exitEvent);
        return exitEvent;
    }

    public long getNumberOfVisits() {
        return statisticDao.getNumberOfVisits();
    }

    public Duration getAverageDuration(LocalDateTime currentTime) {
        return statisticDao.getAverageDuration(currentTime);
    }

    public List<Stats> getStats(LocalDate from, LocalDate to) {
        return statisticDao.getStats(from, to);
    }
}

package services;

import model.Stats;
import storage.CommandApplication;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReportService extends Service {
private final CommandApplication commandApplication;

    public ReportService(Clock clock, CommandApplication commandApplication) {
        super(clock);
        this.commandApplication = commandApplication;
    }

    public long getNumberOfVisits() {
        return commandApplication.getNumberOfVisits();
    }

    public Duration getAverageDuration(LocalDateTime currentTime) {
        return commandApplication.getAverageDuration(currentTime);
    }

    public List<Stats> getStats(LocalDate from, LocalDate to) {
        if (!from.isBefore(to)) {
            throw new AssertionError("\"From\" should be before \"To\"");
        }
        return commandApplication.getStats(from, to);
    }

}

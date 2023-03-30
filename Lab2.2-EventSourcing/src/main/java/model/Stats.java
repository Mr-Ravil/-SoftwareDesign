package model;

import java.time.Duration;
import java.util.Objects;

public class Stats {
    private long numberOfVisits;
    private Duration averageDuration;

    public Stats() {
        this.numberOfVisits = 0;
        this.averageDuration = Duration.ZERO;
    }

    public Stats(long numberOfVisits, Duration averageDuration) {
        this.numberOfVisits = numberOfVisits;
        this.averageDuration = averageDuration;
    }

    public Stats(long numberOfVisits) {
        this.numberOfVisits = 0;
        this.averageDuration = Duration.ZERO;
    }

    public void incrementVisits() {
        ++numberOfVisits;
    }

    public void incrementDuration(Duration duration) {
        averageDuration = averageDuration.plus(duration);
    }

    public long getNumberOfVisits() {
        return numberOfVisits;
    }

    public void setNumberOfVisits(long numberOfVisits) {
        this.numberOfVisits = numberOfVisits;
    }

    public Duration getAverageDuration() {
        return averageDuration;
    }

    public void setAverageDuration(Duration averageDuration) {
        this.averageDuration = averageDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stats stats = (Stats) o;
        return numberOfVisits == stats.numberOfVisits && Objects.equals(averageDuration, stats.averageDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfVisits, averageDuration);
    }
}

package clock;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

public class MockClock extends Clock {
    private Instant now;
    private ZoneId zone;


    public MockClock() {
        this.now = Instant.EPOCH;
        this.zone = ZoneId.of("UTC");
    }

    public MockClock(Instant now, ZoneId zone) {
        this.now = now;
        this.zone = zone;
    }

    public void setNow(Instant now) {
        this.now = now;
    }

    public Clock plusSeconds(long secondsToAdd) {
        this.now = this.now.plusSeconds(secondsToAdd);
        return this;
    }

    public Clock plus(Duration duration) {
        this.now = this.now.plus(duration);
        return this;
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new MockClock(now, zone);
    }

    @Override
    public Instant instant() {
        return now;
    }
}

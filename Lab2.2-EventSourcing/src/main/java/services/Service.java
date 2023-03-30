package services;

import java.time.Clock;

public abstract class Service {
    protected final Clock clock;

    protected Service(Clock clock) {
        this.clock = clock;
    }
}

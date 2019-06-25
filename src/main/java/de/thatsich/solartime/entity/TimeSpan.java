package de.thatsich.solartime.entity;

import java.time.ZonedDateTime;

public class TimeSpan {

    private final ZonedDateTime earlier;
    private final ZonedDateTime later;

    public TimeSpan(ZonedDateTime earlier, ZonedDateTime later) {
        this.earlier = earlier;
        this.later = later;
    }

    public ZonedDateTime getLater() {
        return later;
    }

    public ZonedDateTime getEarlier() {
        return earlier;
    }
}

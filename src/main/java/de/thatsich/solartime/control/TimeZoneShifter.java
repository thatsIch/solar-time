package de.thatsich.solartime.control;

import java.time.ZonedDateTime;

public class TimeZoneShifter {
    ZonedDateTime shiftDayToZoneOfOtherDay(ZonedDateTime toBeShifted, ZonedDateTime otherDay) {
        final var zone = otherDay.getZone();
        final var shifted = toBeShifted.withZoneSameInstant(zone);

        return shifted;
    }
}

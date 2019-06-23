package de.thatsich.solartime.control;

import de.thatsich.solartime.entity.Altitude;

import java.time.ZonedDateTime;
import java.util.Optional;

public class DawnCalculator {

    private final JulianSunriseCalculator julianSunriseCalculator;
    private final DateConverter dateConverter;
    private final TimeZoneShifter timeZoneShifter;

    public DawnCalculator(JulianSunriseCalculator julianSunriseCalculator, DateConverter dateConverter, TimeZoneShifter timeZoneShifter) {
        this.julianSunriseCalculator = julianSunriseCalculator;
        this.dateConverter = dateConverter;
        this.timeZoneShifter = timeZoneShifter;
    }

    /**
     * sunrise
     * civil dawn
     * nautical dawn
     * astronomical dawn
     */
    public Optional<ZonedDateTime> calculateDawnEvent(final ZonedDateTime day, final double latitude, double longitude, Altitude altitude) {
        return this.julianSunriseCalculator.calculateJulianSunrise(day, latitude, longitude, altitude)
                .map(this.dateConverter::toGregorianDate)
                .map(sunrise -> this.timeZoneShifter.shiftDayToZoneOfOtherDay(sunrise, day));
    }
}

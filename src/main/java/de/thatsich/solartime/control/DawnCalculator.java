package de.thatsich.solartime.control;

import de.thatsich.solartime.entity.Altitude;

import java.time.ZonedDateTime;
import java.util.Optional;

public class DawnCalculator {

    private final JulianSunriseCalculator julianSunriseCalculator;
    private final DateConverter dateConverter;

    public DawnCalculator(JulianSunriseCalculator julianSunriseCalculator, DateConverter dateConverter) {
        this.julianSunriseCalculator = julianSunriseCalculator;
        this.dateConverter = dateConverter;
    }

    /**
     * sunrise
     * civil dawn
     * nautical dawn
     * astronomical dawn
     */
    public Optional<ZonedDateTime> calculateDawnEvent(final ZonedDateTime day, final double latitude, double longitude, Altitude altitude) {
        return this.julianSunriseCalculator.calculateJulianSunrise(day, latitude, longitude, altitude)
                .map(jrise -> {
                    // Convert sunset and sunrise to Gregorian dates, in UTC
                    final var localTimeSunrise = this.dateConverter.toGregorianDate(jrise);

                    // Convert the sunset and sunrise to the timezone of the day parameter
                    final var zone = day.getZone();
                    final var sunriseWithMovedTimeZone = localTimeSunrise.withZoneSameInstant(zone);

                    return sunriseWithMovedTimeZone;
                });
    }

}

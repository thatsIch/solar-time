package de.thatsich.solartime.control;

import de.thatsich.solartime.entity.Altitude;

import java.util.Calendar;
import java.util.Optional;

public class DuskCalculator {
    private final JulianSunsetCalculator julianSunsetCalculator;
    private final DateConverter dateConverter;

    public DuskCalculator(JulianSunsetCalculator julianSunsetCalculator, DateConverter dateConverter) {
        this.julianSunsetCalculator = julianSunsetCalculator;
        this.dateConverter = dateConverter;
    }

    /**
     * Calculate the sunrise and sunset times for the given date and given
     * location. This is based on the Wikipedia article on the Sunrise equation.
     *
     * @param day       The day for which to calculate sunrise and sunset
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return a two-element Gregorian Calendar array. The first element is the
     * sunrise, the second element is the sunset. This will return null if there is no sunrise or sunset. (Ex: no sunrise in Antarctica in June)
     * @see <a href="http://en.wikipedia.org/wiki/Sunrise_equation">Sunrise equation on Wikipedia</a>
     */
    public Optional<Calendar> calculateDuskEvent(final Calendar day, final double latitude, double longitude, Altitude altitude) {
        return this.julianSunsetCalculator.calculateJulianSunset(day, latitude, longitude, altitude)
                .map(jset -> {
                    // Convert sunset and sunrise to Gregorian dates, in UTC
                    final Calendar gregSetUTC = this.dateConverter.toGregorianDate(jset);

                    final Calendar gregSet = Calendar.getInstance(day.getTimeZone());
                    gregSet.setTimeInMillis(gregSetUTC.getTimeInMillis());
                    return gregSet;
                });
    }

}

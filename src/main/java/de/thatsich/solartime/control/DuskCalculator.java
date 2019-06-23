package de.thatsich.solartime.control;

import de.thatsich.solartime.entity.Altitude;

import java.time.ZonedDateTime;
import java.util.Optional;

public class DuskCalculator {
    private final JulianSunsetCalculator julianSunsetCalculator;
    private final DateConverter dateConverter;
    private final TimeZoneShifter timeZoneShifter;

    public DuskCalculator(JulianSunsetCalculator julianSunsetCalculator, DateConverter dateConverter, TimeZoneShifter timeZoneShifter) {
        this.julianSunsetCalculator = julianSunsetCalculator;
        this.dateConverter = dateConverter;
        this.timeZoneShifter = timeZoneShifter;
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
    public Optional<ZonedDateTime> calculateDuskEvent(final ZonedDateTime day, final double latitude, double longitude, Altitude altitude) {
        return this.julianSunsetCalculator.calculateJulianSunset(day, latitude, longitude, altitude)
                .map(this.dateConverter::toGregorianDate)
                .map(duskEvent -> this.timeZoneShifter.shiftDayToZoneOfOtherDay(duskEvent, day));
    }

}

package de.thatsich.solartime.boundary;

import de.thatsich.solartime.control.DawnCalculator;
import de.thatsich.solartime.control.DuskCalculator;
import de.thatsich.solartime.control.SolarNoonCalculator;
import de.thatsich.solartime.entity.Altitude;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;


/**
 * Provides methods to determine the sunrise, sunset, civil twilight,
 * nautical twilight, and astronomical twilight times of a given
 * location, or if it is currently day or night at a given location. <br>
 * Also provides methods to convert between Gregorian and Julian dates.<br>
 * The formulas used by this class are from the Wikipedia articles on Julian Day
 * and Sunrise Equation. <br>
 *
 * @author Carmen Alvarez
 * @see <a href="http://en.wikipedia.org/wiki/Julian_day">Julian Day on Wikipedia</a>
 * @see <a href="http://en.wikipedia.org/wiki/Sunrise_equation">Sunrise equation on Wikipedia</a>
 */
@SuppressWarnings({"unused"})
public class SolarTime {

    private final DawnCalculator dawnCalculator;
    private final DuskCalculator duskCalculator;
    private final SolarNoonCalculator solarNoonCalculator;

    public SolarTime(DawnCalculator dawnCalculator, DuskCalculator duskCalculator, SolarNoonCalculator solarNoonCalculator) {
        this.dawnCalculator = dawnCalculator;
        this.duskCalculator = duskCalculator;
        this.solarNoonCalculator = solarNoonCalculator;
    }

    /**
     * Calculate the civil twilight time for the given date and given location.
     *
     * @param day       The day for which to calculate civil twilight
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return a two-element Gregorian Calendar array. The first element is the
     * civil twilight dawn, the second element is the civil twilight dusk.
     * This will return null if there is no civil twilight. (Ex: no twilight in Antarctica in December)
     */
    public Optional<Calendar> calculateCivilDawn(final Calendar day, final double latitude, double longitude) {
        return this.dawnCalculator.calculateDawnEvent(day, latitude, longitude, Altitude.CIVIL);
    }

    public Optional<Calendar> calculateCivilDusk(final Calendar day, final double latitude, double longitude) {
        return this.duskCalculator.calculateDuskEvent(day, latitude, longitude, Altitude.CIVIL);
    }

    /**
     * Calculate the nautical twilight time for the given date and given location.
     *
     * @param day       The day for which to calculate nautical twilight
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return a two-element Gregorian Calendar array. The first element is the
     * nautical twilight dawn, the second element is the nautical twilight dusk.
     * This will return null if there is no nautical twilight. (Ex: no twilight in Antarctica in December)
     */
    public Optional<Calendar> calculateNauticalDawn(final Calendar day, final double latitude, double longitude) {
        return this.dawnCalculator.calculateDawnEvent(day, latitude, longitude, Altitude.NAUTICAL);
    }

    public Optional<Calendar> calculateNauticalDusk(final Calendar day, final double latitude, double longitude) {
        return this.duskCalculator.calculateDuskEvent(day, latitude, longitude, Altitude.NAUTICAL);
    }

    /**
     * Calculate the astronomical twilight time for the given date and given location.
     *
     * @param day       The day for which to calculate astronomical twilight
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return a two-element Gregorian Calendar array. The first element is the
     * astronomical twilight dawn, the second element is the  astronomical twilight dusk.
     * This will return null if there is no astronomical twilight. (Ex: no twilight in Antarctica in December)
     */
    public Optional<Calendar> calculateAstronomicalDawn(final Calendar day, final double latitude, double longitude) {
        return this.dawnCalculator.calculateDawnEvent(day, latitude, longitude, Altitude.ASTRONOMICAL);
    }

    public Optional<Calendar> calculateAstronomicalDusk(final Calendar day, final double latitude, double longitude) {
        return this.duskCalculator.calculateDuskEvent(day, latitude, longitude, Altitude.ASTRONOMICAL);
    }

    public Optional<Calendar> calculateSunrise(final Calendar day, final double latitude, double longitude) {
        return this.dawnCalculator.calculateDawnEvent(day, latitude,longitude, Altitude.SUNRISE_SUNSET);
    }

    public Optional<Calendar> calculateSunset(final Calendar day, final double latitude, double longitude) {
        return this.duskCalculator.calculateDuskEvent(day, latitude, longitude, Altitude.SUNRISE_SUNSET);
    }

    public Optional<Calendar> calculateSolarNoon(final Calendar day, final double latitude, double longitude) {
        return this.solarNoonCalculator.calculateSolarNoon(day, latitude, longitude);
    }

    public Optional<Calendar> calculatePreviousSolarMidnight(final Calendar day, final double latitude, double longitude) {
        final var dayDate = day.getTime();
        final var previousDay = Calendar.getInstance();
        previousDay.setTime(dayDate);
        previousDay.add(Calendar.DAY_OF_MONTH, -1);

        final var maybeDusk = this.calculateAstronomicalDusk(previousDay, latitude, longitude);
        final var maybeDawn = this.calculateAstronomicalDawn(day, latitude, longitude);

        return maybeDusk.flatMap(dusk -> maybeDawn.map(dawn -> calculateMidpoint(dusk, dawn)));
    }

    public Optional<Calendar> calculateNextSolarMidnight(final Calendar day, final double latitude, double longitude) {
        final var dayDate = day.getTime();
        final var nextDay = Calendar.getInstance();
        nextDay.setTime(dayDate);
        nextDay.add(Calendar.DAY_OF_MONTH, 1);

        final var maybeDusk = this.calculateAstronomicalDusk(day, latitude, longitude);
        final var maybeDawn = this.calculateAstronomicalDawn(nextDay, latitude, longitude);

        return maybeDusk.flatMap(dusk -> maybeDawn.map(dawn -> calculateMidpoint(dusk, dawn)));
    }

    private Calendar calculateMidpoint(Calendar first, Calendar second) {
        final var midpoint = GregorianCalendar.getInstance();
        final var midpointInMillis = (first.getTimeInMillis() + second.getTimeInMillis()) / 2;
        midpoint.setTimeInMillis(midpointInMillis);

        return midpoint;
    }

}

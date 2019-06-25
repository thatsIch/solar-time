package de.thatsich.solartime.boundary;

import de.thatsich.solartime.control.DawnCalculator;
import de.thatsich.solartime.control.DuskCalculator;
import de.thatsich.solartime.control.SolarNoonCalculator;
import de.thatsich.solartime.entity.Altitude;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;


/**
 * Provides methods to determine the sunrise, sunset, civil twilight,
 * nautical twilight, and astronomical twilight times of a given
 * location, or if it is currently day or night at a given location. <br>
 * Also provides methods to convert between Gregorian and Julian dates.<br>
 * The formulas used by this class are from the Wikipedia articles on Julian Day
 * and Sunrise Equation. <br>
 *
 * @see <a href="http://en.wikipedia.org/wiki/Julian_day">Julian Day on Wikipedia</a>
 * @see <a href="http://en.wikipedia.org/wiki/Sunrise_equation">Sunrise equation on Wikipedia</a>
 */
@SuppressWarnings({"unused"})
public class SolarTime {

    private final DawnCalculator dawnCalculator;
    private final DuskCalculator duskCalculator;
    private final SolarNoonCalculator solarNoonCalculator;

    SolarTime(DawnCalculator dawnCalculator, DuskCalculator duskCalculator, SolarNoonCalculator solarNoonCalculator) {
        this.dawnCalculator = dawnCalculator;
        this.duskCalculator = duskCalculator;
        this.solarNoonCalculator = solarNoonCalculator;
    }

    public Optional<ZonedDateTime> calculatePreviousSolarMidnight(final ZonedDateTime day, final double latitude, double longitude) {
        final var previousDay = day.minusDays(1);

        final var maybeDusk = this.calculateAstronomicalDusk(previousDay, latitude, longitude);
        final var maybeDawn = this.calculateAstronomicalDawn(day, latitude, longitude);

        return maybeDusk.flatMap(dusk -> maybeDawn.map(dawn -> calculateMidpoint(dusk, dawn)));
    }

    /**
     * Calculate the astronomical twilight time for the given date and given location.
     *
     * @param day       The day for which to calculate astronomical twilight
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     *
     * @return astronomical dawn or empty if there is no astronomical twilight (e.g. no twilight in Antarctica in December)
     */
    public Optional<ZonedDateTime> calculateAstronomicalDawn(final ZonedDateTime day, final double latitude, double longitude) {
        return this.dawnCalculator.calculateDawnEvent(day, latitude, longitude, Altitude.ASTRONOMICAL);
    }

    /**
     * Calculate the nautical twilight time for the given date and given location.
     *
     * @param day       The day for which to calculate nautical twilight
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     *
     * @return nautical dawn or empty if there is no nautical twilight (e.g. no twilight in Antarctica in December)
     */
    public Optional<ZonedDateTime> calculateNauticalDawn(final ZonedDateTime day, final double latitude, double longitude) {
        return this.dawnCalculator.calculateDawnEvent(day, latitude, longitude, Altitude.NAUTICAL);
    }

    /**
     * Calculate the civil twilight time for the given date and given location.
     *
     * @param day       The day for which to calculate civil twilight
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     *
     * @return civil dawn or empty if there is no civil twilight (e.g. no twilight in Antarctica in December)
     */
    public Optional<ZonedDateTime> calculateCivilDawn(final ZonedDateTime day, final double latitude, double longitude) {
        return this.dawnCalculator.calculateDawnEvent(day, latitude, longitude, Altitude.CIVIL);
    }

    public Optional<ZonedDateTime> calculateSunrise(final ZonedDateTime day, final double latitude, double longitude) {
        return this.dawnCalculator.calculateDawnEvent(day, latitude,longitude, Altitude.SUNRISE_SUNSET);
    }

    public Optional<ZonedDateTime> calculateSolarNoon(final ZonedDateTime day, final double latitude, double longitude) {
        return this.solarNoonCalculator.calculateSolarNoon(day, latitude, longitude);
    }

    public Optional<ZonedDateTime> calculateSunset(final ZonedDateTime day, final double latitude, double longitude) {
        return this.duskCalculator.calculateDuskEvent(day, latitude, longitude, Altitude.SUNRISE_SUNSET);
    }

    /**
     * Calculate the civil twilight time for the given date and given location.
     *
     * @param day       The day for which to calculate civil twilight
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     *
     * @return civil dusk or empty if there is no civil twilight (e.g. no twilight in Antarctica in December)
     */
    public Optional<ZonedDateTime> calculateCivilDusk(final ZonedDateTime day, final double latitude, double longitude) {
        return this.duskCalculator.calculateDuskEvent(day, latitude, longitude, Altitude.CIVIL);
    }

    /**
     * Calculate the nautical twilight time for the given date and given location.
     *
     * @param day       The day for which to calculate nautical twilight
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     *
     * @return nautical dusk or empty if there is no nautical twilight (e.g. no twilight in Antarctica in December)
     */
    public Optional<ZonedDateTime> calculateNauticalDusk(final ZonedDateTime day, final double latitude, double longitude) {
        return this.duskCalculator.calculateDuskEvent(day, latitude, longitude, Altitude.NAUTICAL);
    }

    /**
     * Calculate the astronomical twilight time for the given date and given location.
     *
     * @param day       The day for which to calculate astronomical twilight
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     *
     * @return astronomical dusk or empty if there is no astronomical twilight (e.g. no twilight in Antarctica in December)
     */
    public Optional<ZonedDateTime> calculateAstronomicalDusk(final ZonedDateTime day, final double latitude, double longitude) {
        return this.duskCalculator.calculateDuskEvent(day, latitude, longitude, Altitude.ASTRONOMICAL);
    }

    public Optional<ZonedDateTime> calculateNextSolarMidnight(final ZonedDateTime day, final double latitude, double longitude) {
        final var nextDay = day.plusDays(1);

        final var maybeDusk = this.calculateAstronomicalDusk(day, latitude, longitude);
        final var maybeDawn = this.calculateAstronomicalDawn(nextDay, latitude, longitude);

        return maybeDusk.flatMap(dusk -> maybeDawn.map(dawn -> calculateMidpoint(dusk, dawn)));
    }

    private ZonedDateTime calculateMidpoint(ZonedDateTime first, ZonedDateTime second) {
        final var midpointInMillis = (first.toInstant().toEpochMilli() + second.toInstant().toEpochMilli()) / 2;

        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(midpointInMillis), first.getZone());
    }

}

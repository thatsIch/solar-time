package de.thatsich.solartime.boundary;

import de.thatsich.solartime.control.HourAngleCalculator;
import de.thatsich.solartime.control.SolarEquationVariableCalculator;
import de.thatsich.solartime.entity.DayPeriod;
import de.thatsich.solartime.entity.SolarEquationVariables;

import java.time.ZonedDateTime;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SunStateChecker {

    private final SolarTime solarTime;
    private final HourAngleCalculator hourAngleCalculator;
    private final SolarEquationVariableCalculator solarEquationVariableCalculator;

    public SunStateChecker(SolarTime solarTime, HourAngleCalculator hourAngleCalculator, SolarEquationVariableCalculator solarEquationVariableCalculator) {
        this.solarTime = solarTime;
        this.hourAngleCalculator = hourAngleCalculator;
        this.solarEquationVariableCalculator = solarEquationVariableCalculator;
    }

    /**
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return true if it is currently day at the given location. This returns
     * true if the current time at the location is after the sunrise and
     * before the sunset for that location.
     */
    public boolean isDay(double latitude, double longitude) {
        final var now = ZonedDateTime.now();
        return isDay(now, latitude, longitude);
    }

    /**
     * @param calendar  a datetime
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return true if it is day at the given location and given datetime. This returns
     * true if the given datetime at the location is after the sunrise and
     * before the sunset for that location.
     */
    public boolean isDay(ZonedDateTime calendar, double latitude, double longitude) {
        final var maybeSunrise = this.solarTime.calculateSunrise(calendar, latitude, longitude);
        final var maybeSunset = this.solarTime.calculateSunset(calendar, latitude, longitude);

        // In extreme latitudes, there may be no sunrise/sunset time in summer or
        // winter, because it will be day or night 24 hours
        if (maybeSunrise.isEmpty() ||maybeSunset.isEmpty()) {
            int month = calendar.getMonthValue();
            if (latitude > 0) {
                // Always night at the north pole in December
                return 4 <= month && month <= 11; // Always day at the north pole in June
            } else {
                // Always day at the south pole in December
                return month < 4 || 11 < month; // Always night at the south pole in June
            }
        }
        final var sunrise = maybeSunrise.get();
        final var sunset = maybeSunset.get();

        return calendar.isAfter(sunrise) && calendar.isBefore(sunset);
    }

    /**
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return true if it is night at the given location currently. This returns
     * true if the current time at the location is after the astronomical twilight dusk and
     * before the astronomical twilight dawn for that location.
     */
    public boolean isNight(double latitude, double longitude) {
        final var now = ZonedDateTime.now();
        return isNight(now, latitude, longitude);
    }

    /**
     * @param calendar  a datetime
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return true if it is night at the given location and datetime. This returns
     * true if the given datetime at the location is after the astronomical twilight dusk and before
     * the astronomical twilight dawn.
     */
    public boolean isNight(ZonedDateTime calendar, double latitude, double longitude) {
        final var maybeDawn = this.solarTime.calculateAstronomicalDawn(calendar, latitude, longitude);
        final var maybeDusk = this.solarTime.calculateAstronomicalDusk(calendar, latitude, longitude);
        if (maybeDawn.isEmpty() ||maybeDusk.isEmpty()) {
            int month = calendar.getMonthValue();
            if (latitude > 0) {
                // Always night at the north pole in December
                return month < 4 || month > 11; // Always day at the north pole in June
            } else {
                // Always day at the south pole in December
                return month >= 4 && month <= 11; // Always night at the south pole in June
            }
        }

        final var dawn = maybeDawn.get();
        final var dusk = maybeDusk.get();

        return calendar.isBefore(dawn) || calendar.isAfter(dusk);
    }

    /**
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return true if it is currently civil twilight at the current time at the given location.
     * This returns true if the current time at the location is between sunset and civil twilight dusk
     * or between civil twilight dawn and sunrise.
     */
    public boolean isCivilTwilight(double latitude, double longitude) {
        final var today = ZonedDateTime.now();
        return isCivilTwilight(today, latitude, longitude);
    }

    private boolean inBetween(ZonedDateTime now, ZonedDateTime earlier, ZonedDateTime early, ZonedDateTime late, ZonedDateTime later) {
        final var inEarly = now.isAfter(earlier) && now.isBefore(early);
        final var inLater = now.isAfter(late) && now.isBefore(later);

        return inEarly || inLater;
    }

    /**
     * @param calendar the datetime for which to determine if it's civil twilight in the given location
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return true if it is civil twilight at the given location and the given calendar.
     * This returns true if the given time at the location is between sunset and civil twilight dusk
     * or between civil twilight dawn and sunrise.
     */
    public boolean isCivilTwilight(ZonedDateTime calendar, double latitude, double longitude) {
        final var maybeSunset = this.solarTime.calculateSunset(calendar, latitude, longitude);
        final var maybeSunrise = this.solarTime.calculateSunrise(calendar, latitude, longitude);
        final var maybeCivilDusk = this.solarTime.calculateCivilDusk(calendar, latitude, longitude);
        final var maybeCivilDawn = this.solarTime.calculateCivilDawn(calendar, latitude, longitude);

        return maybeSunset
                .flatMap(sunset -> maybeSunrise
                .flatMap(sunrise -> maybeCivilDusk
                .flatMap(civilDusk -> maybeCivilDawn
                .map(civilDawn -> inBetween(calendar, civilDawn, sunrise, sunset, civilDusk))))).
                orElse(false);
    }

    /**
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return true if it is currently nautical twilight at the current time at the given location.
     * This returns true if the current time at the location is between civil and nautical twilight dusk
     * or between nautical and civil twilight dawn.
     */
    public boolean isNauticalTwilight(double latitude, double longitude) {
        final var today = ZonedDateTime.now();
        return isNauticalTwilight(today, latitude, longitude);
    }

    /**
     * @param calendar the datetime for which to determine if it's nautical twilight in the given location
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return true if it is nautical twilight at the given location and the given calendar.
     * This returns true if the given time at the location is between civil and nautical twilight dusk
     * or between nautical and civil twilight dawn.
     */
    public boolean isNauticalTwilight(ZonedDateTime calendar, double latitude, double longitude) {
        final var maybeCivilDusk = this.solarTime.calculateCivilDusk(calendar, latitude, longitude);
        final var maybeCivilDawn = this.solarTime.calculateCivilDawn(calendar, latitude, longitude);
        final var maybeNauticalDawn = this.solarTime.calculateNauticalDawn(calendar, latitude, longitude);
        final var maybeNauticalDusk = this.solarTime.calculateNauticalDusk(calendar, latitude, longitude);

        return maybeNauticalDawn
                .flatMap(nauticalDawn -> maybeNauticalDusk
                .flatMap(nauticalDusk -> maybeCivilDusk
                .flatMap(civilDusk -> maybeCivilDawn
                .map(civilDawn -> inBetween(calendar, nauticalDawn, civilDawn, civilDusk, nauticalDusk))))).
                orElse(false);
    }

    /**
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return true if it is currently astronomical twilight at the current time at the given location.
     * This returns true if the current time at the location is between nautical and astronomical twilight dusk
     * or between astronomical and nautical twilight dawn.
     */
    public boolean isAstronomicalTwilight(double latitude, double longitude) {
        final var today = ZonedDateTime.now();
        return isAstronomicalTwilight(today, latitude, longitude);
    }

    /**
     * @param calendar the datetime for which to determine if it's astronomical twilight in the given location
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return true if it is astronomical twilight at the given location and the given calendar.
     * This returns true if the given time at the location is between nautical and astronomical twilight dusk
     * or between astronomical and nautical twilight dawn.
     */
    public boolean isAstronomicalTwilight(ZonedDateTime calendar, double latitude, double longitude) {
        final var maybeNauticalDawn = this.solarTime.calculateNauticalDawn(calendar, latitude, longitude);
        final var maybeNauticalDusk = this.solarTime.calculateNauticalDusk(calendar, latitude, longitude);
        final var maybeAstronomicalDusk = this.solarTime.calculateAstronomicalDusk(calendar, latitude, longitude);
        final var maybeAstronomicalDawn = this.solarTime.calculateAstronomicalDawn(calendar, latitude, longitude);

        return maybeNauticalDawn
                .flatMap(nauticalDawn -> maybeNauticalDusk
                .flatMap(nauticalDusk -> maybeAstronomicalDusk
                .flatMap(astronomicalDusk -> maybeAstronomicalDawn
                .map(astronomicalDawn -> inBetween(calendar, astronomicalDawn, nauticalDawn, nauticalDusk, astronomicalDusk))))).
                orElse(false);
    }

    /**
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return true if it is civil, nautical, or astronomical twilight currently at the given location.
     */
    public boolean isTwilight(double latitude, double longitude) {
        final var today = ZonedDateTime.now();
        return isTwilight(today, latitude, longitude);
    }

    /**
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @param calendar the given datetime to check for twilight
     * @return true if at the given location and calendar, it is civil, nautical, or astronomical twilight.
     */
    public boolean isTwilight(ZonedDateTime calendar, double latitude, double longitude) {
        return isCivilTwilight(calendar, latitude, longitude)
                || isNauticalTwilight(calendar, latitude, longitude)
                || isAstronomicalTwilight(calendar, latitude, longitude);
    }

    public DayPeriod getDayPeriod(ZonedDateTime calendar, double latitude, double longitude) {
        if (isDay(calendar, latitude, longitude)) return DayPeriod.DAY;
        if (isCivilTwilight(calendar, latitude, longitude)) return DayPeriod.CIVIL_TWILIGHT;
        if (isNauticalTwilight(calendar, latitude, longitude)) return DayPeriod.NAUTICAL_TWILIGHT;
        if (isAstronomicalTwilight(calendar, latitude, longitude)) return DayPeriod.ASTRONOMICAL_TWILIGHT;
        if (isNight(calendar, latitude, longitude)) return DayPeriod.NIGHT;
        return DayPeriod.NIGHT;
    }

    public boolean is24HourDayTime(ZonedDateTime day, double latitude, double longitude) {
        final SolarEquationVariables solarEquationVariables = solarEquationVariableCalculator.calculateSolarEquationVariables(day, longitude);
        final var sunDeclination = solarEquationVariables.getDelta();
        final var rads = Math.toRadians(latitude);

        return hourAngleCalculator.is24HourDayTime(rads, sunDeclination);
    }

    public boolean is24HourNightTime(ZonedDateTime day, double latitude, double longitude) {
        final SolarEquationVariables solarEquationVariables = solarEquationVariableCalculator.calculateSolarEquationVariables(day, longitude);
        final var sunDeclination = solarEquationVariables.getDelta();
        final var rads = Math.toRadians(latitude);

        return hourAngleCalculator.is24HourNightTime(rads, sunDeclination);
    }
}

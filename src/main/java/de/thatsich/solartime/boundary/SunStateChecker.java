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
     * @param calendar  a datetime
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return true if it is day at the given location and given datetime. This returns
     * true if the given datetime at the location is after the sunrise and
     * before the sunset for that location.
     */
    public boolean isDay(ZonedDateTime calendar, double latitude, double longitude) {
        return this.solarTime.calculateSunrise(calendar, latitude, longitude)
                .flatMap(sunrise -> this.solarTime.calculateSunset(calendar, latitude, longitude)
                .map(sunset -> calendar.isAfter(sunrise) && calendar.isBefore(sunset)))
                .orElseGet(() -> isDayAtPoles(calendar, latitude));
    }

    private boolean isDayAtPoles(ZonedDateTime dateTime, double latitude) {
        int month = dateTime.getMonthValue();
        if (latitude > 0) {
            // Always night at the north pole in December
            // Always day at the north pole in June
            return 4 <= month && month <= 11;
        } else {
            // Always day at the south pole in December
            // Always night at the south pole in June
            return month < 4 || 11 < month;
        }
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
        return this.solarTime.calculateAstronomicalDawn(calendar, latitude, longitude)
                .flatMap(dawn -> this.solarTime.calculateAstronomicalDusk(calendar, latitude, longitude)
                .map(dusk -> calendar.isBefore(dawn) || calendar.isAfter(dusk)))
                .orElseGet(() -> isNightAtPoles(calendar, latitude));
    }

    private boolean isNightAtPoles(ZonedDateTime dateTime, double latitude) {
        int month = dateTime.getMonthValue();
        if (latitude > 0) {
            // Always night at the north pole in December
            // Always day at the north pole in June
            return month < 4 || month > 11;
        } else {
            // Always day at the south pole in December
            // Always night at the south pole in June
            return month >= 4 && month <= 11;
        }
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
        return this.solarTime.calculateSunset(calendar, latitude, longitude)
                .flatMap(sunset -> this.solarTime.calculateSunrise(calendar, latitude, longitude)
                .flatMap(sunrise -> this.solarTime.calculateCivilDusk(calendar, latitude, longitude)
                .flatMap(civilDusk -> this.solarTime.calculateCivilDawn(calendar, latitude, longitude)
                .map(civilDawn -> inBetween(calendar, civilDawn, sunrise, sunset, civilDusk))))).
                orElse(false);
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
        return this.solarTime.calculateNauticalDawn(calendar, latitude, longitude)
                .flatMap(nauticalDawn -> this.solarTime.calculateNauticalDusk(calendar, latitude, longitude)
                .flatMap(nauticalDusk -> this.solarTime.calculateCivilDusk(calendar, latitude, longitude)
                .flatMap(civilDusk -> this.solarTime.calculateCivilDawn(calendar, latitude, longitude)
                .map(civilDawn -> inBetween(calendar, nauticalDawn, civilDawn, civilDusk, nauticalDusk))))).
                orElse(false);
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
        return this.solarTime.calculateNauticalDawn(calendar, latitude, longitude)
                .flatMap(nauticalDawn -> this.solarTime.calculateNauticalDusk(calendar, latitude, longitude)
                .flatMap(nauticalDusk -> this.solarTime.calculateAstronomicalDusk(calendar, latitude, longitude)
                .flatMap(astronomicalDusk -> this.solarTime.calculateAstronomicalDawn(calendar, latitude, longitude)
                .map(astronomicalDawn -> inBetween(calendar, astronomicalDawn, nauticalDawn, nauticalDusk, astronomicalDusk))))).
                orElse(false);
    }

    /**
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @param dateTime the given datetime to check for twilight
     * @return true if at the given location and dateTime, it is civil, nautical, or astronomical twilight.
     */
    public boolean isTwilight(ZonedDateTime dateTime, double latitude, double longitude) {
        return isCivilTwilight(dateTime, latitude, longitude)
                || isNauticalTwilight(dateTime, latitude, longitude)
                || isAstronomicalTwilight(dateTime, latitude, longitude);
    }

    public DayPeriod getDayPeriod(ZonedDateTime dateTime, double latitude, double longitude) {
        if (isDay(dateTime, latitude, longitude)) return DayPeriod.DAY;
        if (isCivilTwilight(dateTime, latitude, longitude)) return DayPeriod.CIVIL_TWILIGHT;
        if (isNauticalTwilight(dateTime, latitude, longitude)) return DayPeriod.NAUTICAL_TWILIGHT;
        if (isAstronomicalTwilight(dateTime, latitude, longitude)) return DayPeriod.ASTRONOMICAL_TWILIGHT;
        if (isNight(dateTime, latitude, longitude)) return DayPeriod.NIGHT;

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

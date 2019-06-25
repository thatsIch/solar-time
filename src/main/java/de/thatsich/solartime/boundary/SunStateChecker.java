package de.thatsich.solartime.boundary;

import de.thatsich.solartime.control.HourAngleCalculator;
import de.thatsich.solartime.control.SolarEquationVariableCalculator;
import de.thatsich.solartime.entity.DayPeriod;
import de.thatsich.solartime.entity.SolarEquationVariables;
import de.thatsich.solartime.entity.TimeSpan;

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
     * @param dateTime  a datetime
     * @param latitude  the latitude of the location in degrees.
     * @param longitude the longitude of the location in degrees (West is negative)
     * @return true if it is day at the given location and given datetime. This returns
     * true if the given datetime at the location is after the sunrise and
     * before the sunset for that location.
     */
    public boolean isDay(ZonedDateTime dateTime, double latitude, double longitude) {
        return this.solarTime.calculateSunrise(dateTime, latitude, longitude)
                .flatMap(sunrise -> this.solarTime.calculateSunset(dateTime, latitude, longitude)
                .map(sunset -> dateTime.isAfter(sunrise) && dateTime.isBefore(sunset)))
                .orElseGet(() -> is24HourDayTime(dateTime, latitude, longitude));
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
                .orElseGet(() -> is24HourNightTime(calendar, latitude, longitude));
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
                .map(civilDawn -> inBetween(calendar, new TimeSpan(civilDawn, sunrise), new TimeSpan(sunset, civilDusk)))))).
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
                .map(civilDawn -> inBetween(calendar, new TimeSpan(nauticalDawn, civilDawn), new TimeSpan(civilDusk, nauticalDusk)))))).
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
                .map(astronomicalDawn -> inBetween(calendar, new TimeSpan(astronomicalDawn, nauticalDawn), new TimeSpan(nauticalDusk, astronomicalDusk)))))).
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
        final DayPeriod period;
        if (isDay(dateTime, latitude, longitude)) {
            period = DayPeriod.DAY;
        } else if (isCivilTwilight(dateTime, latitude, longitude)) {
            period = DayPeriod.CIVIL_TWILIGHT;
        } else if (isNauticalTwilight(dateTime, latitude, longitude)) {
            period = DayPeriod.NAUTICAL_TWILIGHT;
        } else if (isAstronomicalTwilight(dateTime, latitude, longitude)) {
            period = DayPeriod.ASTRONOMICAL_TWILIGHT;
        } else if (isNight(dateTime, latitude, longitude)) {
            period = DayPeriod.NIGHT;
        } else {
            period = DayPeriod.NIGHT;
        }

        return period;
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

    private boolean inBetween(ZonedDateTime now, TimeSpan early, TimeSpan late) {
        final var inEarly = now.isAfter(early.getEarlier()) && now.isBefore(early.getLater());
        final var inLater = now.isAfter(late.getEarlier()) && now.isBefore(late.getLater());

        return inEarly || inLater;
    }
}

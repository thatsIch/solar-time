package de.thatsich.solartime.control;

import de.thatsich.solartime.entity.Altitude;

import java.util.Calendar;
import java.util.Optional;

public class SolarNoonCalculator {

    private final SolarEquationVariableCalculator solarCalculator;
    private final HourAngleCalculator hourAngleCalculator;
    private final DateConverter dateConverter;

    public SolarNoonCalculator(SolarEquationVariableCalculator solarCalculator, HourAngleCalculator hourAngleCalculator, DateConverter dateConverter) {
        this.solarCalculator = solarCalculator;
        this.hourAngleCalculator = hourAngleCalculator;
        this.dateConverter = dateConverter;
    }

    /**
     * Calculate the solar noon time for the given date and given location.
     * This is based on the Wikipedia article on the Sunrise equation.
     *
     * @param day         The day for which to calculate sunrise and sunset
     * @param latitude  the latitude of the location in degrees.
     * @param longitude   the longitude of the location in degrees (West is negative)
     * @return            a Calendar with the time set to solar noon for the given day.
     * @see <a href="http://en.wikipedia.org/wiki/Sunrise_equation">Sunrise equation on Wikipedia</a>
     */
    public Optional<Calendar> calculateSolarNoon(final Calendar day, final double latitude, double longitude) {
        final var solarEquationVariables = this.solarCalculator.calculateSolarEquationVariables(day, longitude);

        // Add a check for Antarctica in June and December (sun always down or up, respectively).
        // In this case, jtransit will be filled in, but we need to check the hour angle omega for
        // sunrise.
        // If there's no sunrise (omega is NaN), there's no solar noon.
        final var latitudeRad = Math.toRadians(latitude);

        // Hour angle
        return this.hourAngleCalculator.calculateHourAngle(Altitude.SUNRISE_SUNSET, latitudeRad, solarEquationVariables.getDelta())
                .map(omega -> {
                    // Convert jtransit Gregorian dates, in UTC
                    final var gregNoonUTC = this.dateConverter.toGregorianDate(solarEquationVariables.getJtransit());
                    final var gregNoon = Calendar.getInstance(day.getTimeZone());
                    gregNoon.setTimeInMillis(gregNoonUTC.getTimeInMillis());
                    return gregNoon;
                });
    }

}

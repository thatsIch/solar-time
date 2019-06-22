package de.thatsich.solartime.control;

import de.thatsich.solartime.entity.SolarEquationVariables;

import java.util.Calendar;

import static de.thatsich.solartime.entity.JulianConstants.CONST_0009;
import static de.thatsich.solartime.entity.JulianConstants.CONST_360;
import static de.thatsich.solartime.entity.JulianConstants.JULIAN_DATE_2000_01_01;

public class SolarEquationVariableCalculator {

    private static final double EARTH_MAX_TILT_TOWARDS_SUN = 23.439;

    private final DateConverter dateConverter;

    public SolarEquationVariableCalculator(DateConverter dateConverter) {
        this.dateConverter = dateConverter;
    }

    /**
     * Return intermediate variables used for calculating sunrise, sunset, and solar noon.
     *
     * @param day         The day for which to calculate the ecliptic longitude and jtransit
     * @param longitude   the longitude of the location in degrees (West is negative)
     * @return a 2-element array with the ecliptic longitude (lambda) as the first element, and solar transit (jtransit) as the second element
     * @see <a href="http://en.wikipedia.org/wiki/Sunrise_equation">Sunrise equation on Wikipedia</a>
     */
    public SolarEquationVariables calculateSolarEquationVariables(final Calendar day, double longitude) {

        longitude = -longitude;

        // Get the given date as a Julian date.
        final double julianDate = this.dateConverter.toJulianDate(day);

        // Calculate current Julian cycle (number of days since 2000-01-01).
        final double nstar = julianDate - JULIAN_DATE_2000_01_01 - CONST_0009
                - longitude / CONST_360;
        final double n = Math.round(nstar);

        // Approximate solar noon
        final double jstar = JULIAN_DATE_2000_01_01 + CONST_0009 + longitude
                / CONST_360 + n;
        // Solar mean anomaly
        final double m = Math
                .toRadians((357.5291 + 0.98560028 * (jstar - JULIAN_DATE_2000_01_01))
                        % CONST_360);

        // Equation of center
        final double c = 1.9148 * Math.sin(m) + 0.0200 * Math.sin(2 * m)
                + 0.0003 * Math.sin(3 * m);

        // Ecliptic longitude
        final double lambda = Math
                .toRadians((Math.toDegrees(m) + 102.9372 + c + 180) % CONST_360);

        // Solar transit (hour angle for solar noon)
        final double jtransit = jstar + 0.0053 * Math.sin(m) - 0.0069 * Math.sin(2 * lambda);

        // Declination of the sun.
        final double delta = Math.asin(Math.sin(lambda) * Math.sin(Math.toRadians(EARTH_MAX_TILT_TOWARDS_SUN)));


        return new SolarEquationVariables(n, m, lambda, jtransit, delta);
    }

}

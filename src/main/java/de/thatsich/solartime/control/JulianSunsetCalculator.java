package de.thatsich.solartime.control;

import de.thatsich.solartime.entity.Altitude;
import de.thatsich.solartime.entity.SolarEquationVariables;

import java.time.ZonedDateTime;
import java.util.Optional;

import static de.thatsich.solartime.entity.JulianConstants.CONST_0009;
import static de.thatsich.solartime.entity.JulianConstants.CONST_360;
import static de.thatsich.solartime.entity.JulianConstants.JULIAN_DATE_2000_01_01;

public class JulianSunsetCalculator {

    private final SolarEquationVariableCalculator solarCalculator;
    private final HourAngleCalculator hourAngleCalculator;

    public JulianSunsetCalculator(SolarEquationVariableCalculator solarCalculator, HourAngleCalculator hourAngleCalculator) {
        this.solarCalculator = solarCalculator;
        this.hourAngleCalculator = hourAngleCalculator;
    }

    Optional<Double> calculateJulianSunset(final ZonedDateTime day, final double latitude, double longitude, Altitude altitude) {
        final SolarEquationVariables solarEquationVariables = this.solarCalculator.calculateSolarEquationVariables(day, longitude);

        final var inverted = -longitude;
        final double latitudeRad = Math.toRadians(latitude);

        // Hour angle
        final Optional<Double> maybeOmega = this.hourAngleCalculator.calculateHourAngle(altitude, latitudeRad, solarEquationVariables.getDelta());

        return maybeOmega.map(omega -> {
            // Sunset
            final double jset = JULIAN_DATE_2000_01_01
                    + CONST_0009
                    + ((Math.toDegrees(omega) + inverted) / CONST_360 + solarEquationVariables.getN() + 0.0053
                    * Math.sin(solarEquationVariables.getM()) - 0.0069 * Math.sin(2 * solarEquationVariables.getLambda()));

            return jset;
        });
    }

}

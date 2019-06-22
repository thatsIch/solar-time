package de.thatsich.solartime.control;

import de.thatsich.solartime.entity.Altitude;
import de.thatsich.solartime.entity.SolarEquationVariables;

import java.time.ZonedDateTime;
import java.util.Optional;

public class JulianSunriseCalculator {

    private final JulianSunsetCalculator julianSunsetCalculator;
    private final SolarEquationVariableCalculator solarEquationVariableCalculator;

    public JulianSunriseCalculator(JulianSunsetCalculator julianSunsetCalculator, SolarEquationVariableCalculator solarEquationVariableCalculator) {
        this.julianSunsetCalculator = julianSunsetCalculator;
        this.solarEquationVariableCalculator = solarEquationVariableCalculator;
    }

    Optional<Double> calculateJulianSunrise(final ZonedDateTime day, final double latitude, double longitude, Altitude altitude) {
        final Optional<Double> maybeSunset = this.julianSunsetCalculator.calculateJulianSunset(day, latitude, longitude, altitude);
        return maybeSunset.map(sunset -> {
            final SolarEquationVariables solarEquationVariables = this.solarEquationVariableCalculator.calculateSolarEquationVariables(day, longitude);

            // Sunrise
            final double jrise = solarEquationVariables.getJtransit() - (sunset - solarEquationVariables.getJtransit());

            return jrise;
        });
    }

}

package de.thatsich.solartime.boundary;

import de.thatsich.solartime.control.DateConverter;
import de.thatsich.solartime.control.DawnCalculator;
import de.thatsich.solartime.control.DuskCalculator;
import de.thatsich.solartime.control.HourAngleCalculator;
import de.thatsich.solartime.control.JulianSunriseCalculator;
import de.thatsich.solartime.control.JulianSunsetCalculator;
import de.thatsich.solartime.control.SolarEquationVariableCalculator;
import de.thatsich.solartime.control.SolarNoonCalculator;
import de.thatsich.solartime.control.TimeZoneShifter;

public class API {

    public SolarTime getSolarTime() {
        final var dateConverter = new DateConverter();
        final var solarEquationVariableCalculator = new SolarEquationVariableCalculator(dateConverter);
        final var hourAngleCalculator = new HourAngleCalculator();
        final var julianSunsetCalculator = new JulianSunsetCalculator(solarEquationVariableCalculator, hourAngleCalculator);
        final var julianSunriseCalculator = new JulianSunriseCalculator(julianSunsetCalculator, solarEquationVariableCalculator);
        final var timeZoneShifter = new TimeZoneShifter();
        final var duskCalculator = new DuskCalculator(julianSunsetCalculator, dateConverter, timeZoneShifter);
        final var dawnCalculator = new DawnCalculator(julianSunriseCalculator, dateConverter, timeZoneShifter);
        final var solarNoonCalculator = new SolarNoonCalculator(solarEquationVariableCalculator, hourAngleCalculator, dateConverter);
        final var solarTime = new SolarTime(dawnCalculator, duskCalculator, solarNoonCalculator);

        return solarTime;
    }

    public SunStateChecker getSunStateChecker() {
        final var solarTime = this.getSolarTime();
        final var hourAngleCalculator = new HourAngleCalculator();
        final var dateConverter = new DateConverter();
        final var solarEquationVariableCalculator = new SolarEquationVariableCalculator(dateConverter);
        final var sunStateChecker = new SunStateChecker(solarTime, hourAngleCalculator, solarEquationVariableCalculator);

        return sunStateChecker;
    }

}

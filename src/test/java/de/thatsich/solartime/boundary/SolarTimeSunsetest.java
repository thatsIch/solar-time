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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.time.ZonedDateTime;

class SolarTimeSunsetest {

    private SolarTime setupSolarTime() {
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

    @DisplayName("sunset exists")
    @ParameterizedTest(name = "{index}. {0} => day={1}, latitude={2}, longitude={3}")
    @CsvFileSource(numLinesToSkip = 1, resources = "./sunrise-sunet.csv")
    void testSunriseExists(@SuppressWarnings("unused") String location, ZonedDateTime day, double latitude, double longitude) {
        final var solarTime = this.setupSolarTime();

        final var actual = solarTime.calculateSunset(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isPresent();
    }

    @DisplayName("sunrise exists")
    @ParameterizedTest(name = "{index}. {0} => day={1}, latitude={2}, longitude={3}, sunrise={4}, sunset={5}")
    @CsvFileSource(numLinesToSkip = 1, resources = "./sunrise-sunet.csv")
    void testSunriseIsExact(@SuppressWarnings("unused") String location, ZonedDateTime day, double latitude, double longitude, @SuppressWarnings("unused") ZonedDateTime sunrise, ZonedDateTime sunset) {
        final var solarTime = this.setupSolarTime();

        final var actual = solarTime.calculateSunset(day, latitude, longitude);

        Assertions.assertThat(actual)
                .contains(sunset);
    }

}
package de.thatsich.solartime.boundary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.time.ZonedDateTime;

class SolarTimeSunsetTest {

    @DisplayName("sunset exists")
    @ParameterizedTest(name = "{index}. {0} => day={1}, latitude={2}, longitude={3}")
    @CsvFileSource(numLinesToSkip = 1, resources = "./sunrise-sunet.csv")
    void testSunriseExists(@SuppressWarnings("unused") String location, ZonedDateTime day, double latitude, double longitude) {
        final var solarTime = new API().getSolarTime();

        final var actual = solarTime.calculateSunset(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isPresent();
    }

    @DisplayName("sunrise exists")
    @ParameterizedTest(name = "{index}. {0} => day={1}, latitude={2}, longitude={3}, sunrise={4}, sunset={5}")
    @CsvFileSource(numLinesToSkip = 1, resources = "./sunrise-sunet.csv")
    void testSunriseIsExact(@SuppressWarnings("unused") String location, ZonedDateTime day, double latitude, double longitude, @SuppressWarnings("unused") ZonedDateTime sunrise, ZonedDateTime sunset) {
        final var solarTime = new API().getSolarTime();

        final var actual = solarTime.calculateSunset(day, latitude, longitude);

        Assertions.assertThat(actual)
                .contains(sunset);
    }

}
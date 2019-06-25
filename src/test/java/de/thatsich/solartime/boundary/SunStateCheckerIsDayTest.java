package de.thatsich.solartime.boundary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.ZoneId;
import java.time.ZonedDateTime;

class SunStateCheckerIsDayTest {

    @Test
    @DisplayName("At 2019-06-24T12:00:00+02:00[Europe/Berlin] is Day in Europe")
    void happyPath_dayIsAfterSunriseAndBeforeSunset() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 6, 24, 12, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.isDay(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isTrue();
    }

    @Test
    @DisplayName("At 2019-06-24T01:00:00+02:00[Europe/Berlin] is before Day in Europe")
    void beforeDay() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 6, 24, 1, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.isDay(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isFalse();
    }

    @Test
    @DisplayName("At 2019-06-24T23:00:00+02:00[Europe/Berlin] is after Day in Europe")
    void afterDay() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 6, 24, 23, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.isDay(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isFalse();
    }

    @DisplayName("North Pole")
    @ParameterizedTest(name = "Month {0} should be {1}")
    @CsvSource({ "1, false", "2, true", "3, true", "4, true", "5, true", "6, true", "7, false", "8, true", "9, true", "10, true", "11, false", "12, false"})
    void checkNorthPole(int month, boolean expected) {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, month, 24, 12, 0, 0, 0, ZoneId.of("Europe/Istanbul"));
        final var latitude = 69.660716;
        final var longitude = 18.925278;

        final var actual = sunStateChecker.isDay(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isEqualTo(expected);
    }


    @DisplayName("South Pole")
    @ParameterizedTest(name = "Month {0} should be {1}")
    @CsvSource({ "1, true", "2, true", "3, false", "4, false", "5, false", "6, false", "7, false", "8, false", "9, true", "10, true", "11, true", "12, true"})
    void checkSouthPole(int month, boolean expected) {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, month, 24, 12, 0, 0, 0, ZoneId.of("Europe/Istanbul"));
        final var latitude = -90;
        final var longitude = 0;

        final var actual = sunStateChecker.isDay(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isEqualTo(expected);
    }


}
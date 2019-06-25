package de.thatsich.solartime.boundary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@DisplayName("Is Night")
class SunStateCheckerIsNightTest {

    @Test
    @DisplayName("Before dawn is night")
    void beforeDawn() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 1, 24, 1, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.isNight(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isTrue();
    }

    @Test
    @DisplayName("After dusk is night")
    void afterDusk() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 1, 24, 23, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.isNight(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isTrue();
    }

    @Test
    @DisplayName("While day is no night")
    void whileDay() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 1, 24, 12, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.isNight(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isFalse();
    }

}
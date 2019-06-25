package de.thatsich.solartime.boundary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

class SunStateCheckerIsDayTest {

    @Test
    void isDay() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 6, 24, 12, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.isDay(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isTrue();
    }

    @Test
    void isNotDay() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 6, 24, 1, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.isDay(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isFalse();
    }

    @Test
    @DisplayName("In Tromsø, Norway there is a 24h Night")
    void alwaysNightAtPoles() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 12, 24, 12, 0, 0, 0, ZoneId.of("Europe/Istanbul"));
        final var latitude = 69.660716;
        final var longitude = 18.925278;

        final var actual = sunStateChecker.isDay(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isFalse();
    }

}
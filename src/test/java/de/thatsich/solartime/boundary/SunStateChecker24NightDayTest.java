package de.thatsich.solartime.boundary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

class SunStateChecker24NightDayTest {

    @Test
    @DisplayName("In Europe there is no 24h Night")
    void isAbsent() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 6, 24, 12, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.is24HourNightTime(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isFalse();
    }

    @Test
    @DisplayName("In Tromsø, Norway there is a 24h Night")
    void isPresent() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 12, 24, 12, 0, 0, 0, ZoneId.of("Europe/Istanbul"));
        final var latitude = 69.660716;
        final var longitude = 18.925278;

        final var actual = sunStateChecker.is24HourNightTime(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isTrue();
    }
}
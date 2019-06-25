package de.thatsich.solartime.boundary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

class SunStateCheckerIsDayTest {

    @Test
    @DisplayName("At 2019-06-24T12:00:00+02:00[Europe/Berlin] is Day in Europe")
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

        final var day = ZonedDateTime.of(2019, 6, 24, 1, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.isDay(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isFalse();
    }

    @Test
    @DisplayName("In Tromsø, Norway there is a 24h Night")
    void alwaysNightAtNorthPoles() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 12, 24, 12, 0, 0, 0, ZoneId.of("Europe/Istanbul"));
        final var latitude = 69.660716;
        final var longitude = 18.925278;

        final var actual = sunStateChecker.isDay(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isFalse();
    }

    @Test
    @DisplayName("Always day at the south pole in december")
    void alwaysDayAtSouthPoleInDecember() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 12, 24, 12, 0, 0, 0, ZoneId.of("Europe/Istanbul"));
        final var latitude = -90;
        final var longitude = 0;

        final var actual = sunStateChecker.isDay(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isTrue();
    }

}
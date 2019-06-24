package de.thatsich.solartime.boundary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SunStateChecker24HourDayTest {

    @Test
    @DisplayName("In Europe there is no 24h Day")
    void isNot24HourDayTime() {
        final var sunStateChecker = API.getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 6, 24, 12, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.is24HourDayTime(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isFalse();
    }

    @Test
    @DisplayName("In Nunavut, Canada there is a 24h Day")
    void is24HourDayTime() {
        final var sunStateChecker = API.getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 6, 24, 12, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 82.481306;
        final var longitude = -62.239533;

        final var actual = sunStateChecker.is24HourDayTime(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isTrue();
    }
}
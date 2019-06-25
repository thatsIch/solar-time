package de.thatsich.solartime.boundary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

class SunStateCheckerAstronomicalTwilightTest {

    @Test
    @DisplayName("At 2019-01-24T6:30:00+02:00[Europe/Berlin] is Astronomical Twilight in Europe")
    void isNight() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 1, 24, 6, 30, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.isAstronomicalTwilight(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isTrue();
    }

}
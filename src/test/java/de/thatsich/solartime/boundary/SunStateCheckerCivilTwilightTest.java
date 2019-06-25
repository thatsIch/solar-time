package de.thatsich.solartime.boundary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

class SunStateCheckerCivilTwilightTest {

    @Test
    @DisplayName("At 2019-06-24T05:00:00+02:00[Europe/Berlin] is a Civil Twilight in Europe")
    void civilTwilight() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 6, 24, 5, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.isCivilTwilight(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isTrue();
    }

}
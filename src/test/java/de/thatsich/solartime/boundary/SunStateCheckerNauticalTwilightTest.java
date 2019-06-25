package de.thatsich.solartime.boundary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

class SunStateCheckerNauticalTwilightTest {

    @Test
    @DisplayName("At 2019-06-24T3:16:00+02:00[Europe/Berlin] is Nautical Twilight in Europe")
    void isNight() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 6, 24, 3, 16, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.isNauticalTwilight(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isTrue();
    }

}
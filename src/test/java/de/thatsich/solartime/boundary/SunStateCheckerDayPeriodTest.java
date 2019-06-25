package de.thatsich.solartime.boundary;

import de.thatsich.solartime.entity.DayPeriod;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

class SunStateCheckerDayPeriodTest {

    @Test
    void isDay() {
        final var sunStateChecker = new API().getSunStateChecker();

        final var day = ZonedDateTime.of(2019, 6, 24, 12, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        final var actual = sunStateChecker.getDayPeriod(day, latitude, longitude);

        Assertions.assertThat(actual)
                .isEqualTo(DayPeriod.DAY);
    }

}
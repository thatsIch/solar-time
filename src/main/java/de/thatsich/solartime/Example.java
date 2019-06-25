package de.thatsich.solartime;

import de.thatsich.solartime.boundary.API;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

class Example {

    private static final Logger LOGGER = Logger.getLogger("Example");

    public static void main(String[] args) {
        final var api = new API();
        final var solarTime = api.getSolarTime();

        final var now = ZonedDateTime.now();
        final var latitude = 51.449680;
        final var longitude = 6.973370;

        solarTime.calculatePreviousSolarMidnight(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .map(ZonedDateTime::toLocalDateTime)
                .ifPresent(prevSolarMidnight -> LOGGER.info("Previous Solar Midnight: " + prevSolarMidnight));

        solarTime.calculateAstronomicalDawn(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .map(ZonedDateTime::toLocalDateTime)
                .ifPresent(time -> LOGGER.info("Astronomical Dawn: " + time));

        solarTime.calculateNauticalDawn(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .map(ZonedDateTime::toLocalDateTime)
                .ifPresent(time -> LOGGER.info("Nautical Dawn: " + time));

        solarTime.calculateCivilDawn(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> LOGGER.info("Civil Dawn: " + time));

        solarTime.calculateSunrise(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> LOGGER.info("Sunrise: " + time));

        solarTime.calculateSolarNoon(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> LOGGER.info("Solar Noon: " + time));

        solarTime.calculateSunset(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> LOGGER.info("Sunset: " + time));

        solarTime.calculateCivilDusk(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> LOGGER.info("Civil Dusk: " + time));

        solarTime.calculateNauticalDusk(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> LOGGER.info("Nautical Dusk: " + time));

        solarTime.calculateAstronomicalDusk(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> LOGGER.info("Astronomical Dusk: " + time));

        solarTime.calculateNextSolarMidnight(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> LOGGER.info("Next Solar Midnight: " + time));

        final var sunStateChecker = api.getSunStateChecker();
        LOGGER.info("is 24-hour day: " + sunStateChecker.is24HourDayTime(now, latitude, longitude));
        LOGGER.info("is 24-hour night: " + sunStateChecker.is24HourNightTime(now, latitude, longitude));
    }
}

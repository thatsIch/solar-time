package de.thatsich.solartime;

import de.thatsich.solartime.boundary.SolarTime;
import de.thatsich.solartime.control.DateConverter;
import de.thatsich.solartime.control.DawnCalculator;
import de.thatsich.solartime.control.DuskCalculator;
import de.thatsich.solartime.control.HourAngleCalculator;
import de.thatsich.solartime.control.JulianSunriseCalculator;
import de.thatsich.solartime.control.JulianSunsetCalculator;
import de.thatsich.solartime.control.SolarEquationVariableCalculator;
import de.thatsich.solartime.control.SolarNoonCalculator;
import de.thatsich.solartime.entity.SolarEquationVariables;

import java.time.ZoneId;
import java.time.ZonedDateTime;

class Example {
    public static void main(String[] args) {
        final var dateConverter = new DateConverter();
        final var solarEquationVariableCalculator = new SolarEquationVariableCalculator(dateConverter);
        final var hourAngleCalculator = new HourAngleCalculator();
        final var julianSunsetCalculator = new JulianSunsetCalculator(solarEquationVariableCalculator, hourAngleCalculator);
        final var julianSunriseCalculator = new JulianSunriseCalculator(julianSunsetCalculator, solarEquationVariableCalculator);
        final var duskCalculator = new DuskCalculator(julianSunsetCalculator, dateConverter);
        final var dawnCalculator = new DawnCalculator(julianSunriseCalculator, dateConverter);
        final var solarNoonCalculator = new SolarNoonCalculator(solarEquationVariableCalculator, hourAngleCalculator, dateConverter);
        final var solarTime = new SolarTime(dawnCalculator, duskCalculator, solarNoonCalculator);

        final var now = ZonedDateTime.now();
        final var latitude = 51.449680;
        final var longitude = 6.973370;
        //final var latitude = -89.237462;
        //final var longitude = 144.809434;

        solarTime.calculatePreviousSolarMidnight(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .map(ZonedDateTime::toLocalDateTime)
                .ifPresent(prevSolarMidnight -> System.out.println("Previous Solar Midnight: " + prevSolarMidnight));

        solarTime.calculateAstronomicalDawn(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .map(ZonedDateTime::toLocalDateTime)
                .ifPresent(time -> System.out.println("Astronomical Dawn: " + time));

        solarTime.calculateNauticalDawn(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .map(ZonedDateTime::toLocalDateTime)
                .ifPresent(time -> System.out.println("Nautical Dawn: " + time));

        solarTime.calculateCivilDawn(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> System.out.println("Civil Dawn: " + time));

        solarTime.calculateSunrise(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> System.out.println("Sunrise: " + time));

        solarTime.calculateSolarNoon(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> System.out.println("Solar Noon: " + time));

        solarTime.calculateSunset(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> System.out.println("Sunset: " + time));

        solarTime.calculateCivilDusk(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> System.out.println("Civil Dusk: " + time));

        solarTime.calculateNauticalDusk(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> System.out.println("Nautical Dusk: " + time));

        solarTime.calculateAstronomicalDusk(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> System.out.println("Astronomical Dusk: " + time));

        solarTime.calculateNextSolarMidnight(now, latitude, longitude)
                .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
                .ifPresent(time -> System.out.println("Next Solar Midnight: " + time));

        final SolarEquationVariables solarEquationVariables = solarEquationVariableCalculator.calculateSolarEquationVariables(now, longitude);
        final var sunDeclination = solarEquationVariables.getDelta();
        System.out.println(hourAngleCalculator.is24HourDayTime( Math.toRadians(latitude), sunDeclination));
        System.out.println(hourAngleCalculator.is24HourNightTime( Math.toRadians(latitude), sunDeclination));
    }
}

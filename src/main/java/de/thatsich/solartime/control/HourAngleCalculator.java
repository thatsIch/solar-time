package de.thatsich.solartime.control;

import de.thatsich.solartime.entity.Altitude;

import java.util.Optional;

public class HourAngleCalculator {

    Optional<Double> calculateHourAngle(Altitude altitude, double latitudeRad, double sunDeclination) {
        final var radAltitude = Math.toRadians(altitude.getValue());
        final var omega = Math.acos((Math.sin(radAltitude) - Math.sin(latitudeRad) * Math.sin(sunDeclination))
                / (Math.cos(latitudeRad) * Math.cos(sunDeclination)));

        if (Double.isNaN(omega)) {
            return Optional.empty();
        }
        else {
            return Optional.of(omega);
        }
    }

    public boolean is24HourDayTime(double latitudeRad, double sunDeclination) {
        return Math.tan(latitudeRad) * Math.tan(sunDeclination) > 1;
    }

    public boolean is24HourNightTime(double latitudeRad, double sunDeclination) {
        return Math.tan(latitudeRad) * Math.tan(sunDeclination) < -1;
    }

}

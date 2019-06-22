package de.thatsich.solartime.control;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

public class DateConverter {

    /**
     * Convert a Gregorian calendar date to a Julian date. Accuracy is to the
     * second.
     * <br>
     * This is based on the Wikipedia article for Julian day.
     *
     * @param gregorianDate Gregorian date in any time zone.
     * @return the Julian date for the given Gregorian date.
     * @see <a href="http://en.wikipedia.org/wiki/Julian_day#Converting_Julian_or_Gregorian_calendar_date_to_Julian_Day_Number">Converting to Julian day number on Wikipedia</a>
     */
    double toJulianDate(final Calendar gregorianDate) {
        // Convert the date to the UTC time zone.
        TimeZone tzUTC = TimeZone.getTimeZone("UTC");
        Calendar gregorianDateUTC = Calendar.getInstance(tzUTC);
        gregorianDateUTC.setTimeInMillis(gregorianDate.getTimeInMillis());
        // For the year (Y) astronomical year numbering is used, thus 1 BC is 0,
        // 2 BC is -1, and 4713 BC is -4712.
        int year = gregorianDateUTC.get(Calendar.YEAR);
        // The months (M) January to December are 1 to 12
        int month = gregorianDateUTC.get(Calendar.MONTH) + 1;
        // D is the day of the month.
        int day = gregorianDateUTC.get(Calendar.DAY_OF_MONTH);
        int a = (14 - month) / 12;
        int y = year + 4800 - a;
        int m = month + 12 * a - 3;

        int julianDay = day + (153 * m + 2) / 5 + 365 * y + (y / 4) - (y / 100)
                + (y / 400) - 32045;
        int hour = gregorianDateUTC.get(Calendar.HOUR_OF_DAY);
        int minute = gregorianDateUTC.get(Calendar.MINUTE);
        int second = gregorianDateUTC.get(Calendar.SECOND);

        return julianDay + ((double) hour - 12) / 24
                + ((double) minute) / 1440 + ((double) second) / 86400;
    }

    /**
     * Convert a Julian date to a Gregorian date. The Gregorian date will be in
     * the local time zone. Accuracy is to the second.
     * <br>
     * This is based on the Wikipedia article for Julian day.
     *
     * @param julianDate The date to convert
     * @return a Gregorian date in the local time zone.
     * @see <a href="http://en.wikipedia.org/wiki/Julian_day#Gregorian_calendar_from_Julian_day_number">Converting from Julian day to Gregorian date, on Wikipedia</a>
     */
    Calendar toGregorianDate(final double julianDate) {

        final int DAYS_PER_4000_YEARS = 146097;
        final int DAYS_PER_CENTURY = 36524;
        final int DAYS_PER_4_YEARS = 1461;
        final int DAYS_PER_5_MONTHS = 153;

        // Let J = JD + 0.5: (note: this shifts the epoch back by one half day,
        // to start it at 00:00UTC, instead of 12:00 UTC);
        int J = (int) (julianDate + 0.5);

        // let j = J + 32044; (note: this shifts the epoch back to astronomical
        // year -4800 instead of the start of the Christian era in year AD 1 of
        // the proleptic Gregorian calendar).
        int j = J + 32044;

        // let g = j div 146097; let dg = j mod 146097;
        int g = j / DAYS_PER_4000_YEARS;
        int dg = j % DAYS_PER_4000_YEARS;

        // let c = (dg div 36524 + 1) * 3 div 4; let dc = dg - c * 36524;
        int c = ((dg / DAYS_PER_CENTURY + 1) * 3) / 4;
        int dc = dg - c * DAYS_PER_CENTURY;

        // let b = dc div 1461; let db = dc mod 1461;
        int b = dc / DAYS_PER_4_YEARS;
        int db = dc % DAYS_PER_4_YEARS;

        // let a = (db div 365 + 1) * 3 div 4; let da = db - a * 365;
        int a = ((db / 365 + 1) * 3) / 4;
        int da = db - a * 365;

        // let y = g * 400 + c * 100 + b * 4 + a; (note: this is the integer
        // number of full years elapsed since March 1, 4801 BC at 00:00 UTC);
        int y = g * 400 + c * 100 + b * 4 + a;

        // let m = (da * 5 + 308) div 153 - 2; (note: this is the integer number
        // of full months elapsed since the last March 1 at 00:00 UTC);
        int m = (da * 5 + 308) / DAYS_PER_5_MONTHS - 2;

        // let d = da -(m + 4) * 153 div 5 + 122; (note: this is the number of
        // days elapsed since day 1 of the month at 00:00 UTC, including
        // fractions of one day);
        int d = da - ((m + 4) * DAYS_PER_5_MONTHS) / 5 + 122;

        // let Y = y - 4800 + (m + 2) div 12;
        int year = y - 4800 + (m + 2) / 12;

        // let M = (m + 2) mod 12 + 1;
        int month = (m + 2) % 12;

        // let D = d + 1;
        int day = d + 1;

        // Apply the fraction of the day in the Julian date to the Gregorian
        // date.
        // Example: dayFraction = 0.717
        final double dayFraction = (julianDate + 0.5) - J;

        // Ex: 0.717*24 = 17.208 hours. We truncate to 17 hours.
        final int hours = (int) (dayFraction * 24);
        // Ex: 17.208 - 17 = 0.208 days. 0.208*60 = 12.48 minutes. We truncate
        // to 12 minutes.
        final int minutes = (int) ((dayFraction * 24 - hours) * 60d);
        // Ex: 17.208*60 - (17*60 + 12) = 1032.48 - 1032 = 0.48 minutes. 0.48*60
        // = 28.8 seconds.
        // We round to 29 seconds.
        final int seconds = (int) ((dayFraction * 24 * 3600 - (hours * 3600 + minutes * 60)) + .5);

        // Create the gregorian date in UTC.
        final var gregorianDateUTC = ZonedDateTime.of(year, month, day, hours, minutes, seconds, 0, ZoneOffset.UTC);
        final Calendar gregorianDateUTC = Calendar.getInstance(TimeZone
                .getTimeZone("UTC"));
        gregorianDateUTC.set(Calendar.YEAR, year);
        gregorianDateUTC.set(Calendar.MONTH, month);
        gregorianDateUTC.set(Calendar.DAY_OF_MONTH, day);
        gregorianDateUTC.set(Calendar.HOUR_OF_DAY, hours);
        gregorianDateUTC.set(Calendar.MINUTE, minutes);
        gregorianDateUTC.set(Calendar.SECOND, seconds);
        gregorianDateUTC.set(Calendar.MILLISECOND, 0);

        // Convert to a Gregorian date in the local time zone.
        Calendar gregorianDate = Calendar.getInstance();
        gregorianDate.setTimeInMillis(gregorianDateUTC.getTimeInMillis());
        return gregorianDate;
    }
}

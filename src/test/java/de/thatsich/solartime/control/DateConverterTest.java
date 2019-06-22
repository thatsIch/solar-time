package de.thatsich.solartime.control;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;


@DisplayName("Test conversion between gregorian and julian date")
class DateConverterTest {

    @DisplayName("gregorian to julian date conversion")
    @ParameterizedTest(name = "{index} => gregorian={0}, expected={1}")
    @CsvFileSource(numLinesToSkip = 1, resources = "./gregorian-to-julian-date.csv")
    void toJulianDate(ZonedDateTime gregorian, double expected) {
        final var converter = new DateConverter();

        final var actual = converter.toJulianDate(gregorian);

        Assertions.assertThat(actual)
                .isEqualTo(expected);
    }

    @DisplayName("julian to gregorian conversion")
    @ParameterizedTest(name = "{index} => expected={0}, julian={1}")
    @CsvFileSource(numLinesToSkip = 1, resources = "./gregorian-to-julian-date.csv")
    void toGregorianDate(ZonedDateTime expected, double julian) {
        final var converter = new DateConverter();

        final var actual = converter.toGregorianDate(julian)
                .toInstant()
                .atZone(ZoneId.systemDefault());

        Assertions.assertThat(actual)
                .isEqualTo(expected);
    }

    @DisplayName("Chained julian to gregorian. Should result in the same")
    @ParameterizedTest(name = "{index} => gregorian={0}")
    @CsvFileSource(numLinesToSkip = 1, resources = "./gregorian-to-julian-date.csv")
    void chaingedGregorianToJulian(ZonedDateTime gregorian) {
        final var converter = new DateConverter();

        final var julian = converter.toJulianDate(gregorian);
        final var actualGregorian = converter.toGregorianDate(julian)
                .toInstant()
                .atZone(ZoneOffset.UTC);

        Assertions.assertThat(actualGregorian)
                .isEqualTo(gregorian);
    }

    @DisplayName("Chained gregorian to julian. Should result in the same")
    @ParameterizedTest(name = "{index} => gregorian={0}, expected={1}")
    @CsvFileSource(numLinesToSkip = 1, resources = "./gregorian-to-julian-date.csv")
    void chaingedJulianToGregorian(@SuppressWarnings("unused") ZonedDateTime ignored, double julian) {
        final var converter = new DateConverter();

        final var gregorian = converter.toGregorianDate(julian);
        final var actualJulian = converter.toJulianDate(gregorian);

        Assertions.assertThat(actualJulian)
                .isEqualTo(julian);
    }
}
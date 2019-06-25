# Solar Time
Java Library, Calculate solar times like solar noon, sunrise, sunset, astronomical, nautical and civil dawn/dusks

---

This work is heavily inspired by https://github.com/caarmen/SunriseSunset. The fundamental changes are:

* requires Java 11+,
* usage of Java 8 Time API (`ZonedDateTime` instead of `Calendar`),
* splitting return results of dawn and dusk and
* usage of `Optional` because `Empty` is a valid result for solar times.

## Project Status

[![Maintenance](https://img.shields.io/maintenance/yes/2019.svg)](https://github.com/thatsIch/solar-time)

[![Build Status](https://travis-ci.org/thatsIch/solar-time.svg?branch=master)](https://travis-ci.org/thatsIch/solar-time)

[![Codecov Coverage](https://codecov.io/gh/thatsIch/solar-time/branch/master/graph/badge.svg)](https://codecov.io/gh/thatsIch/solar-time)
[![Coverage Status](https://coveralls.io/repos/github/thatsIch/solar-time/badge.svg?branch=master&kill_cache=1)](https://coveralls.io/github/thatsIch/solar-time?branch=master)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=thatsIch_solar-time&metric=coverage)](https://sonarcloud.io/component_measures?id=thatsIch_solar-time&metric=coverage&view=list)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=thatsIch_solar-time&metric=alert_status)](https://sonarcloud.io/dashboard?id=thatsIch_solar-time)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=thatsIch_solar-time&metric=security_rating)](https://sonarcloud.io/dashboard?id=thatsIch_solar-time)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=thatsIch_solar-time&metric=sqale_index)](https://sonarcloud.io/dashboard?id=thatsIch_solar-time)
[![Maintainability](https://api.codeclimate.com/v1/badges/7bebabb71ca31cf2aa8d/maintainability)](https://codeclimate.com/github/thatsIch/solar-time/maintainability)

## Example Usage

Entrypoint is the `API`. You can instatiate it via `new API()` to retrieve the `SolarTime` and `SunStateChecker` endpoints.

```java
final var api = new API();
final var solarTime = api.getSolarTime();

final var now = ZonedDateTime.now();
final var latitude = 51.449680;
final var longitude = 6.973370;

solarTime.calculateNauticalDawn(now, latitude, longitude)
        .map(dateTime -> dateTime.withZoneSameInstant(ZoneId.systemDefault()))
        .map(ZonedDateTime::toLocalDateTime)
        .ifPresent(time -> LOGGER.info("Nautical Dawn: " + time));
```

> INFO: Nautical Dawn: 2019-06-25T03:15:46

```java
final var sunStateChecker = api.getSunStateChecker();
LOGGER.info("is 24-hour day: " + sunStateChecker.is24HourDayTime(now, latitude, longitude));
```

> INFO: is 24-hour day: false

For more examples check the [wiki](https://github.com/thatsIch/solar-time/wiki/Examples)
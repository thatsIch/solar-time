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
[![Coverage Status](https://coveralls.io/repos/github/thatsIch/solar-time/badge.svg?branch=master)](https://coveralls.io/github/thatsIch/solar-time?branch=master)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=thatsIch_solar-time&metric=coverage)](https://sonarcloud.io/dashboard?id=thatsIch_solar-time)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=thatsIch_solar-time&metric=alert_status)](https://sonarcloud.io/dashboard?id=thatsIch_solar-time)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=thatsIch_solar-time&metric=security_rating)](https://sonarcloud.io/dashboard?id=thatsIch_solar-time)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=thatsIch_solar-time&metric=sqale_index)](https://sonarcloud.io/dashboard?id=thatsIch_solar-time)
[![Maintainability](https://api.codeclimate.com/v1/badges/7bebabb71ca31cf2aa8d/maintainability)](https://codeclimate.com/github/thatsIch/solar-time/maintainability)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/cb6ede108aca42b7a0683c8a55c10bb2)](https://www.codacy.com/app/thatsIch/solar-time?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=thatsIch/solar-time&amp;utm_campaign=Badge_Grade)
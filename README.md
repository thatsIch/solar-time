# Solar Time
Java Library, Calculate solar times like solar noon, sunrise, sunset, astronomical, nautical and civil dawn/dusks

---

This work is heavily inspired by https://github.com/caarmen/SunriseSunset. The fundamental changes are:

* requires Java 11+,
* usage of Java 8 Time API (`LocalDateTime`, `ZonedDateTime` instead of `Calendar`),
* splitting return results of dawn and dusk and
* usage of `Optional` because `Empty` is a valid result for solar times.

## Project Status

[![Maintenance](https://img.shields.io/maintenance/yes/2019.svg)](https://github.com/thatsIch/solar-time)

[![Build Status](https://travis-ci.org/thatsIch/solar-time.svg?branch=master)](https://travis-ci.org/thatsIch/solar-time)

[![codecov](https://codecov.io/gh/thatsIch/solar-time/branch/master/graph/badge.svg)](https://codecov.io/gh/thatsIch/solar-time)

package de.thatsich.solartime.boundary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class APISmokeTest {

    @Test
    void setupSolarTime() {
        Assertions.assertThatCode(API::getSolarTime)
                .doesNotThrowAnyException();
    }

    @Test
    void setupSunStateChecker() {
        Assertions.assertThatCode(API::getSunStateChecker)
                .doesNotThrowAnyException();
    }

}

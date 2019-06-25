package de.thatsich.solartime.boundary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class APISmokeTest {

    @Test
    void setupSolarTime() {
        Assertions.assertThatCode(() -> new API().getSolarTime())
                .doesNotThrowAnyException();
    }

    @Test
    void setupSunStateChecker() {
        Assertions.assertThatCode(() -> new API().getSunStateChecker())
                .doesNotThrowAnyException();
    }

}

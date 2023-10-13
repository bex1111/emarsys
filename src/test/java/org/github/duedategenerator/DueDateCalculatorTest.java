package org.github.duedategenerator;

import org.github.duedategenerator.exception.SubmitDateNullException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DueDateCalculatorTest {

    @Test
    void submitDateNull() {
        assertThatThrownBy(() -> new DueDateCalculator().calculate(null))
                .isInstanceOf(SubmitDateNullException.class);
    }

}

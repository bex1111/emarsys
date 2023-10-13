package org.github.duedategenerator;

import org.github.duedategenerator.exception.OutOfWorkingHourException;
import org.github.duedategenerator.exception.SubmitDateNullException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DueDateCalculatorTest {

    @Test
    void submitDateNull() {
        assertThatThrownBy(() -> new DueDateCalculator().calculate(null))
                .isInstanceOf(SubmitDateNullException.class);
    }

    @Test
    void submitDateIsAfterFivePm() {
        assertThatThrownBy(() -> new DueDateCalculator()
                .calculate(LocalDateTime.of(2023,3,3,17,1)))
                .isInstanceOf(OutOfWorkingHourException.class);
    }

    @Test
    void submitDateIsAfterNineAm() {
        assertThatThrownBy(() -> new DueDateCalculator()
                .calculate(LocalDateTime.of(2023,3,3,8,59)))
                .isInstanceOf(OutOfWorkingHourException.class);
    }

}

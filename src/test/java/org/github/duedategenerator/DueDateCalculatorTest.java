package org.github.duedategenerator;

import org.github.duedategenerator.exception.NotWorkingDayException;
import org.github.duedategenerator.exception.OutOfWorkingHourException;
import org.github.duedategenerator.exception.SubmitDateNullException;
import org.github.duedategenerator.exception.TurnaroundTimeNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DueDateCalculatorTest {

    private DueDateCalculator dueDateCalculator;

    @BeforeEach
    void setUp() {
        dueDateCalculator = new DueDateCalculator();
    }

    @Test
    void submitDateNull() {
        assertThatThrownBy(() -> dueDateCalculator.calculate(null, null))
                .isInstanceOf(SubmitDateNullException.class);
    }

    @Test
    void submitDateIsAfterFivePm() {
        assertThatThrownBy(() -> dueDateCalculator
                .calculate(LocalDateTime.of(2023, 3, 3, 17, 1), null))
                .isInstanceOf(OutOfWorkingHourException.class);
    }

    @Test
    void submitDateIsAfterNineAm() {
        assertThatThrownBy(() -> dueDateCalculator
                .calculate(LocalDateTime.of(2023, 3, 3, 8, 59), null))
                .isInstanceOf(OutOfWorkingHourException.class);
    }

    private static Stream<LocalDateTime> weekendLocalDateTimeProvider() {
        var saturday = LocalDateTime.of(2023, 10, 14, 10, 59);
        var sunday = LocalDateTime.of(2023, 10, 15, 10, 59);
        return Stream.of(saturday, sunday);
    }

    @ParameterizedTest
    @MethodSource("weekendLocalDateTimeProvider")
    void submitDateIsNotWorkDay(LocalDateTime localDateTime) {
        assertThatThrownBy(() -> dueDateCalculator
                .calculate(localDateTime, null))
                .isInstanceOf(NotWorkingDayException.class);
    }

    @Test
    void turnaroundTimeNull() {
        assertThatThrownBy(() -> dueDateCalculator
                .calculate(LocalDateTime.of(2023, 3, 3, 10, 59), null))
                .isInstanceOf(TurnaroundTimeNullException.class);
    }

    @Test
    void resolveIssueInTheDay() {
        assertThat(dueDateCalculator
                .calculate(LocalDateTime.of(2023, 3, 3, 9, 0), 8L))
                .isEqualTo(LocalDateTime.of(2023, 3, 3, 17, 0));
    }
}

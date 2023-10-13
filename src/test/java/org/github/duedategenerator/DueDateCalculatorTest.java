package org.github.duedategenerator;

import org.github.duedategenerator.exception.NotWorkingDayException;
import org.github.duedategenerator.exception.OutOfWorkingHourException;
import org.github.duedategenerator.exception.SubmitDateNullException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.stream.Stream;

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

    private static Stream<LocalDateTime> weekendLocalDateTimeProvider(){
        var saturday =LocalDateTime.of(2023,10,14,10,59);
        var sunday =LocalDateTime.of(2023,10,15,10,59);
        return Stream.of(saturday,sunday);
    }

    @ParameterizedTest
    @MethodSource("weekendLocalDateTimeProvider")
    void submitDateIsNotWorkDay(LocalDateTime localDateTime) {
        assertThatThrownBy(() -> new DueDateCalculator()
                .calculate(localDateTime))
                .isInstanceOf(NotWorkingDayException.class);
    }

}

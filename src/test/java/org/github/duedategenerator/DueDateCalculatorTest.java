package org.github.duedategenerator;

import org.github.duedategenerator.exception.NotWorkingDayException;
import org.github.duedategenerator.exception.OutOfWorkingHourException;
import org.github.duedategenerator.exception.SubmitDateNullException;
import org.github.duedategenerator.exception.TurnaroundTimeNullException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DueDateCalculatorTest {

    public static final int YEAR = 2023;

    @Test
    void submitDateNull() {
        assertThatThrownBy(() -> createDueDateCalculator(null))
                .isInstanceOf(SubmitDateNullException.class);
    }

    @Test
    void submitDateIsAfterFivePm() {
        assertThatThrownBy(() -> createDueDateCalculator(LocalDateTime.of(YEAR, 10, 9, 17, 1)))
                .isInstanceOf(OutOfWorkingHourException.class);
    }

    @Test
    void submitDateIsAfterNineAm() {
        assertThatThrownBy(() -> createDueDateCalculator(LocalDateTime.of(YEAR, 10, 9, 8, 59)))
                .isInstanceOf(OutOfWorkingHourException.class);
    }

    private static Stream<LocalDateTime> weekendLocalDateTimeProvider() {
        var saturday = LocalDateTime.of(YEAR, 10, 14, 10, 59);
        var sunday = LocalDateTime.of(YEAR, 10, 15, 10, 59);
        return Stream.of(saturday, sunday);
    }

    @ParameterizedTest
    @MethodSource("weekendLocalDateTimeProvider")
    void submitDateIsNotWorkDay(LocalDateTime localDateTime) {

        assertThatThrownBy(() -> createDueDateCalculator(localDateTime))
                .isInstanceOf(NotWorkingDayException.class);
    }

    @Test
    void turnaroundTimeNull() {
        assertThatThrownBy(() -> createDueDateCalculator(
                LocalDateTime.of(YEAR, 10, 9, 10, 59), null))
                .isInstanceOf(TurnaroundTimeNullException.class);
    }

    private static Stream<DueDateCalculatorTestDto> resolveIssueLessThanWorkingWeekProvider() {
        return Stream.of(
                new DueDateCalculatorTestDto(8L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 10, 9, 17, 0)),
                new DueDateCalculatorTestDto(9L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 10, 10, 10, 0)),
                new DueDateCalculatorTestDto(16L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 10, 10, 17, 0)),
                new DueDateCalculatorTestDto(20L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 10, 11, 13, 0)),
                new DueDateCalculatorTestDto(40L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 10, 13, 17, 0)),
                new DueDateCalculatorTestDto(0L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 10, 9, 9, 0)),
                new DueDateCalculatorTestDto(12L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 10, 10, 13, 0))
        );
    }

    @ParameterizedTest
    @MethodSource("resolveIssueLessThanWorkingWeekProvider")
    void resolveIssueLessThanWorkingWeek(DueDateCalculatorTestDto testDto) {
        assertThat(
                createDueDateCalculator(testDto.submitDate(), testDto.turnaroundTime()).calculate())
                .isEqualTo(testDto.resolvedIssueTime());
    }


    private static Stream<DueDateCalculatorTestDto> resolveIssueMoreThanWorkingWeekProvider() {
        return Stream.of(
                new DueDateCalculatorTestDto(41L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 10, 16, 10, 0)),
                new DueDateCalculatorTestDto(49L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 10, 17, 10, 0)),
                new DueDateCalculatorTestDto(80L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 10, 20, 17, 0)),
                new DueDateCalculatorTestDto(81L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 10, 23, 10, 0)),
                new DueDateCalculatorTestDto(89L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 10, 24, 10, 0)),
                new DueDateCalculatorTestDto(160L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 11, 3, 17, 0)),
                new DueDateCalculatorTestDto(163L,
                        LocalDateTime.of(YEAR, 10, 9, 9, 0),
                        LocalDateTime.of(YEAR, 11, 6, 12, 0))
        );
    }


    @ParameterizedTest
    @MethodSource("resolveIssueMoreThanWorkingWeekProvider")
    void resolveIssueMoreThanWorkingWeek(DueDateCalculatorTestDto testDto) {
        assertThat(createDueDateCalculator(testDto.submitDate(), testDto.turnaroundTime()).calculate())
                .isEqualTo(testDto.resolvedIssueTime());
    }


    private DueDateCalculator createDueDateCalculator(LocalDateTime submitDate, Long turnaroundTime) {
        return new DueDateCalculator(submitDate, turnaroundTime);
    }

    private void createDueDateCalculator(LocalDateTime submitDate) {
        new DueDateCalculator(submitDate, 1L);
    }
}

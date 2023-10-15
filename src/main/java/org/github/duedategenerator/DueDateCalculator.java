package org.github.duedategenerator;

import org.github.duedategenerator.exception.NotWorkingDayException;
import org.github.duedategenerator.exception.OutOfWorkingHourException;
import org.github.duedategenerator.exception.SubmitDateNullException;
import org.github.duedategenerator.exception.TurnaroundTimeNullException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.util.Objects.isNull;

public class DueDateCalculator {

    public static final int DAY_WORKING_HOURS = 8;
    private final LocalDateTime submitDate;
    private final Long turnaroundTime;

    public DueDateCalculator(LocalDateTime submitDate, Long turnaroundTimeInHours) {
        validateSubmitDate(submitDate);
        validateTurnaroundTime(turnaroundTimeInHours);
        this.submitDate = submitDate;
        this.turnaroundTime = turnaroundTimeInHours;
    }

    public LocalDateTime calculate() {
        final var submitDateWithAdditionalHours = calculateAdditionalHours();
        final var submitDateWithAdditionalDay = calculateAdditionalDay(submitDateWithAdditionalHours);
        return resolveWeekend(submitDateWithAdditionalDay);
    }

    private long calculateAdditionalHoursThatDay() {
        return turnaroundTime % DAY_WORKING_HOURS;
    }

    private LocalDateTime calculateAdditionalHours() {
        if (isOvertime()) {
            return calculateAdditionalHoursSkipOverTime();
        }
        return calculateAdditionalHoursWhenNotHaveOvertime();
    }

    private LocalDateTime calculateAdditionalHoursWhenNotHaveOvertime() {
        final var hours = calculateAdditionalHoursThatDay();
        return submitDate.plusHours(turnaroundTime > 0 && hours == 0 ? DAY_WORKING_HOURS : hours);
    }

    private LocalDateTime calculateAdditionalHoursSkipOverTime() {
        LocalDateTime skipOvertimeLocalDateTime= submitDate.plusDays(1);
        LocalDateTime startHourLocalDateTime= skipOvertimeLocalDateTime.withHour(9);
        return startHourLocalDateTime.plusHours((submitDate.getHour() + calculateAdditionalHoursThatDayForOvertime()) - 17);
    }

    private long calculateAdditionalHoursThatDayForOvertime() {
        long calculateAdditionalHoursThatDay = calculateAdditionalHoursThatDay();
        return turnaroundTime > 0 && calculateAdditionalHoursThatDay == 0 ? 8L : calculateAdditionalHoursThatDay;
    }

    private boolean isOvertime() {
        return submitDate.getHour() + calculateAdditionalHoursThatDayForOvertime() > 17;
    }

    private LocalDateTime resolveWeekend(LocalDateTime submitDateWithAdditionalDay) {
        final var weekWorkingHours = 40;
        var additionalDay = 0L;
        if (turnaroundTime > weekWorkingHours) {
            additionalDay += (turnaroundTime / weekWorkingHours) * 2;
        }
        if (turnaroundTime % weekWorkingHours == 0 && turnaroundTime > weekWorkingHours) {
            additionalDay -= 2;
        }
        return submitDateWithAdditionalDay.plusDays(additionalDay);
    }

    private LocalDateTime calculateAdditionalDay(LocalDateTime submitDateWithAdditionalHours) {
        final var hours = calculateAdditionalHoursThatDay();
        if (turnaroundTime <= DAY_WORKING_HOURS) {
            return submitDateWithAdditionalHours.plusDays(0);
        }
        if (hours == 0) {
            return submitDateWithAdditionalHours.plusDays(turnaroundTime / DAY_WORKING_HOURS - 1);
        }
        return submitDateWithAdditionalHours.plusDays(turnaroundTime / DAY_WORKING_HOURS);
    }

    private void validateSubmitDate(LocalDateTime submitDate) {
        if (isNull(submitDate)) {
            throw new SubmitDateNullException();
        }
        if (isAfterWorkHour(submitDate) || isBeforeWorkHour(submitDate)) {
            throw new OutOfWorkingHourException();
        }
        if (isWeekend(submitDate.getDayOfWeek())) {
            throw new NotWorkingDayException();
        }
    }

    private void validateTurnaroundTime(Long turnaroundTime) {
        if (isNull(turnaroundTime)) {
            throw new TurnaroundTimeNullException();
        }
    }

    private boolean isWeekend(DayOfWeek dayOfWeek) {
        return List.of(SATURDAY, SUNDAY).contains(dayOfWeek);
    }

    private boolean isBeforeWorkHour(LocalDateTime submitDate) {
        return submitDate.getHour() < 9;
    }

    private boolean isAfterWorkHour(LocalDateTime submitDate) {
        return submitDate.getHour() == 17 && submitDate.getMinute() != 0 || submitDate.getHour() > 17;
    }
}

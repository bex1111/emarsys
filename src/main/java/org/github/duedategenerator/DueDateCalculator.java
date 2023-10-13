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

    public LocalDateTime calculate(LocalDateTime submitDate, Long turnaroundTime) {
        validateSubmitDate(submitDate);
        validateTurnaroundTime(turnaroundTime);
        LocalDateTime submitDateWithAdditionalHours = calculateSubmitDateWithAdditionalHours(submitDate, turnaroundTime);
        return calculateSubmitDateWithAdditionalDay(turnaroundTime, submitDateWithAdditionalHours);
    }

    private long calculateAdditionalHours(Long turnaroundTime) {
        return turnaroundTime % 8;
    }

    private LocalDateTime calculateSubmitDateWithAdditionalHours(LocalDateTime submitDate, Long turnaroundTime) {
        final long hours = calculateAdditionalHours(turnaroundTime);
        return submitDate.plusHours(turnaroundTime > 0 && hours == 0 ? 8 : hours);
    }

    private LocalDateTime calculateSubmitDateWithAdditionalDay(Long turnaroundTime, LocalDateTime submitDateWithAdditionalHours) {
        var submitDateWithAdditionalDay = calculateAdditionalDay(turnaroundTime, submitDateWithAdditionalHours);
        return resolveWeekend(turnaroundTime, submitDateWithAdditionalDay);
    }

    private LocalDateTime resolveWeekend(Long turnaroundTime, LocalDateTime submitDateWithAdditionalDay) {
        long additionalDay = 0L;
        if (turnaroundTime > 40) {
            additionalDay += (turnaroundTime / 40) * 2;
        }
        if (turnaroundTime%40==0 && turnaroundTime>40)
        {
            additionalDay -= 2;
        }
        return submitDateWithAdditionalDay.plusDays(additionalDay);
    }

    private LocalDateTime calculateAdditionalDay(Long turnaroundTime, LocalDateTime submitDateWithAdditionalHours) {
        final long hours = calculateAdditionalHours(turnaroundTime);
        if (turnaroundTime <= 8) {
            return submitDateWithAdditionalHours.plusDays(0);
        }
        if (hours == 0) {
            return submitDateWithAdditionalHours.plusDays(turnaroundTime / 8 - 1);
        }
        return submitDateWithAdditionalHours.plusDays(turnaroundTime / 8);
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
        return submitDate.getHour() >= 17 && submitDate.getMinute() != 0;
    }
}

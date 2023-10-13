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
        if (isNull(submitDate)) {
            throw new SubmitDateNullException();
        }
        if (isAfterWorkHour(submitDate) || isBeforeWorkHour(submitDate)) {
            throw new OutOfWorkingHourException();
        }
        if (isWeekend(submitDate.getDayOfWeek())) {
            throw new NotWorkingDayException();
        }
        if (isNull(turnaroundTime)) {
            throw new TurnaroundTimeNullException();
        }
        long hours = turnaroundTime % 8;
        LocalDateTime day = submitDate.plusHours(turnaroundTime > 0 && hours == 0 ? 8 : hours);
        if (turnaroundTime <= 8) {
            return day.plusDays(0);
        }
        if (hours == 0) {
            return day.plusDays(turnaroundTime / 8 - 1);
        }
        return day.plusDays(turnaroundTime / 8);
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

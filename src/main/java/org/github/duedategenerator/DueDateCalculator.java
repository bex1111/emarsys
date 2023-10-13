package org.github.duedategenerator;

import org.github.duedategenerator.exception.NotWorkingDayException;
import org.github.duedategenerator.exception.OutOfWorkingHourException;
import org.github.duedategenerator.exception.SubmitDateNullException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.util.Objects.isNull;

public class DueDateCalculator {
    public void calculate(LocalDateTime submitDate) {
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

package org.github.duedategenerator;

import org.github.duedategenerator.exception.OutOfWorkingHourException;
import org.github.duedategenerator.exception.SubmitDateNullException;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

public class DueDateCalculator {
    public void calculate(LocalDateTime submitDate) {
        if (isNull(submitDate)) {
            throw new SubmitDateNullException();
        }
        if (isAfterWorkHour(submitDate) || isBeforeWorkHour(submitDate)) {
            throw new OutOfWorkingHourException();
        }
    }

    private boolean isBeforeWorkHour(LocalDateTime submitDate) {
        return submitDate.getHour() < 9;
    }

    private boolean isAfterWorkHour(LocalDateTime submitDate) {
        return submitDate.getHour() >= 17 && submitDate.getMinute() != 0;
    }
}

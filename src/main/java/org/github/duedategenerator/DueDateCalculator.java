package org.github.duedategenerator;

import org.github.duedategenerator.exception.SubmitDateNullException;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

public class DueDateCalculator {
    public void calculate(LocalDateTime submitDate) {
        if (isNull(submitDate))
        {
            throw new SubmitDateNullException();
        }
    }
}

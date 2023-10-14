package org.github.duedategenerator;

import java.time.LocalDateTime;

public record DueDateCalculatorTestDto(Long turnaroundTime, LocalDateTime submitDate,LocalDateTime resolvedIssueTime) {
}

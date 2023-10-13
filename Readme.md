# Due Date Calculator

Thanks for handing in your application to a developer position at Emarsys. With this
brief exercise we would like to assess your skills and capabilities on how you can
implement algorithms and write production codes.

# The problem
We are looking for a solution that implements a due date calculator in an issuetracking
system. Your task is to implement the CalculateDueDate method:
• Input: Takes the submit date/time and turnaround time.
• Output: Returns the date/time when the issue is resolved.

# Rules
• Working hours are from 9AM to 5PM on every working day, Monday to Friday.
• Holidays should be ignored (e.g. A holiday on a Thursday is considered as a
working day. A working Saturday counts as a non-working day.).
• The turnaround time is defined in working hours (e.g. 2 days equal 16 hours).
If a problem was reported at 2:12PM on Tuesday and the turnaround time is
16 hours, then it is due by 2:12PM on Thursday.
• A problem can only be reported during working hours. (e.g. All submit date
values are set between 9AM to 5PM.)
• Do not use any third-party libraries for date/time calculations (e.g. Moment.js,
Carbon, Joda, etc.) or hidden functionalities of the built-in methods.

# Additional info
• Use your favourite programming language.
• Do not implement the user interface or CLI.
• Do not write a pseudo code. Write a code that you would commit/push to a
repository and which solves the given problem.
• You have 72 hours to submit your solution.
• You can submit your solution even if you have not finished it fully.

# Bonus – Not mandatory
• Including automated tests to your solution is a plus.
• Test-driven (TDD) solutions are especially welcome.
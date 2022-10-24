package com.kuehne.nagel.coin.desk.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {
    public static void validateDate(final String dateStr,final DateTimeFormatter dateFormatter) {
        if (dateStr == null || dateStr.isEmpty()) {
            throw new IllegalArgumentException("Date is not given");
        }
        try {
            LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Please enter a valid date", "", 0, e);

        }
    }

    public static void validate(final int duration) {
        if (duration < 0) {
            throw new NumberFormatException("Not a valid duration");
        }
    }

    public static int validateAndReturn(final String durationStr) {
        try {
            final int duration = Integer.parseInt(durationStr.trim());
            validate(duration);
            return duration;
        } catch (Exception e) {
            throw new NumberFormatException("Not a valid duration");
        }
    }
}
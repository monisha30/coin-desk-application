package com.kuehne.nagel.coin.desk.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {
    public static void validateDate(String dateStr, DateTimeFormatter dateFormatter){
        if(dateStr == null || dateStr.isEmpty()){
            throw new IllegalArgumentException("Date is not given");
        }
        try {
            LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Please enter a valid date","",0,e);

        }
    }

    public static void validate(int duration) {
        if (duration < 0) {
            throw new NumberFormatException("Not a valid duration");
        }
    }
}
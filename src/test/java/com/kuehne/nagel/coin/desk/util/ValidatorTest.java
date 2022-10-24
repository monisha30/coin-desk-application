package com.kuehne.nagel.coin.desk.util;

import com.kuehne.nagel.coin.desk.service.BitCoinServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest extends Mockito {
    @Test
    void testValidateDate() {
        assertThrows(IllegalArgumentException.class, () -> Validator.validateDate(null, null));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateDate("", null));
    }

    @Test
    void testValidateDate_when_dateTime_parse_exception() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BitCoinServiceImpl.DATE_FORMATTER);
        assertThrows(DateTimeParseException.class, () -> Validator.validateDate("99-09-09", formatter));
    }

    @Test
    void testValidate2() {
        assertThrows(NumberFormatException.class, () -> Validator.validate(-1));
    }
}


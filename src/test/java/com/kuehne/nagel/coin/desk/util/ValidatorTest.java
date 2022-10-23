package com.kuehne.nagel.coin.desk.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ValidatorTest {
    @Test
    void testValidateDate() {
        assertThrows(IllegalArgumentException.class, () -> Validator.validateDate(null, null));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateDate("", null));
    }

    @Test
    void testValidate2() {
        assertThrows(NumberFormatException.class, () -> Validator.validate(-1));
    }
}


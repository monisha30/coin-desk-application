package com.kuehne.nagel.coin.desk.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilTest {

    @Test
    void testGetApiResponse() {
        assertThrows(IllegalArgumentException.class, () -> Util.getApiResponse(null));
        assertThrows(IllegalArgumentException.class, () -> Util.getApiResponse(""));
    }

    @Test
    void testValidateCurrency2() {
        assertThrows(IllegalArgumentException.class, () -> Util.validateCurrency(null));
    }

    @Test
    void testValidateCurrency3() {
        assertThrows(IllegalArgumentException.class, () -> Util.validateCurrency(""));
    }
}


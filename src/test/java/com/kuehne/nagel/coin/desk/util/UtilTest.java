package com.kuehne.nagel.coin.desk.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.kuehne.nagel.coin.desk.service.CoinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.kuehne.nagel.coin.desk.exception.CurrencyNoSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilTest {

    @Test
    void testGetApiResponse() throws IOException, InterruptedException {
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


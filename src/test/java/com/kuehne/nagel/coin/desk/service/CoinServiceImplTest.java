package com.kuehne.nagel.coin.desk.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertThrows;


class CoinServiceImplTest extends Mockito {
    @Test
    void test_print_message() {
        final CoinService coinService = Mockito.spy(CoinService.class);
        final Map<String, Double> historicalMap = new TreeMap<>();

        when(coinService.getLowestRate(historicalMap)).thenReturn(Optional.of(1.0));
        when(coinService.getHighestRate(historicalMap)).thenReturn(Optional.of(4.0));
        coinService.startProcess("INR", "2018-09-09", 90);
        verify(coinService, times(1)).startProcess("INR", "2018-09-09", 90);
    }

    @Test
    void test_print_message_when_exception() {
        final CoinService coinService = Mockito.spy(CoinService.class);
        final Map<String, Double> historicalMap = new TreeMap<>();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BitCoinServiceImpl.DATE_FORMATTER);
        final String startDateStr = "2018-09-09";
        final int duration = 90;
        final LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        final String currency = "EUR";
        final LocalDate endDate = startDate.plusDays(duration);

        when(coinService.getHistoricalDetails(currency, startDate, endDate, duration)).thenReturn(historicalMap);
        when(coinService.getLowestRate(historicalMap)).thenReturn(Optional.empty());
        when(coinService.getHighestRate(historicalMap)).thenReturn(Optional.of(4.0));
        //coinService.startProcess(currency, startDateStr, 90);
        assertThrows(NoSuchElementException.class, () -> coinService.startProcess(currency, startDateStr, duration));

        when(coinService.getLowestRate(historicalMap)).thenReturn(Optional.of(1.0));
        when(coinService.getHighestRate(historicalMap)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> coinService.startProcess(currency, startDateStr, duration));
        //verify(coinService, times(1)).startProcess("INR", "2018-09-09", 90);
    }


}

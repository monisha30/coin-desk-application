package com.kuehne.nagel.coin.desk.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;


public class CoinServiceImplTest extends Mockito {
    @Test
    void test_print_message()  {
        final CoinService coinService = Mockito.spy(CoinService.class);
        final Map<String, Double> historicalMap = new TreeMap<>();

        when(coinService.getLowestRate(historicalMap)).thenReturn(Optional.of(1.0));
        when(coinService.getHighestRate(historicalMap)).thenReturn(Optional.of(4.0));
        coinService.startProcess("INR", "2018-09-09", 90);
        verify(coinService, times(1)).startProcess("INR", "2018-09-09", 90);
    }


}

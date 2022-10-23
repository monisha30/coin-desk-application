package com.kuehne.nagel.coin.desk.service;

import com.kuehne.nagel.coin.desk.exception.CurrencyNoSupportedException;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
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
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CoinServiceImplTest extends Mockito {


    @Test
    void test_print_message() throws IOException, InterruptedException {
       CoinService coinService = Mockito.spy(CoinService.class);


        Map<String,Double> historicalMap = new TreeMap<>();
        //IntStream stream = IntStream.range(1, 5);
        //stream.forEach(i -> historicalMap.put(String.valueOf(i),Double.valueOf(i)));
        when(coinService.getLowestRate(historicalMap)).thenReturn(Optional.of(1.0));
        when(coinService.getHighestRate(historicalMap)).thenReturn(Optional.of(4.0));
        coinService.startProcess("INR","2018-09-09",90);

           // doNothing().when(coinService.printMessage("INR",startDate,endDate,98d,50D,100D,90));
        //Mockito.verify(coinService).printMessage("INR",startDate,endDate,98d,50D,100D,90);
         verify(coinService,times(1)).startProcess("INR","2018-09-09",90);
        }







}

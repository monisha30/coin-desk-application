package com.kuehne.nagel.coin.desk.service;

import com.kuehne.nagel.coin.desk.exception.CurrencyNoSupportedException;
import com.kuehne.nagel.coin.desk.exception.InternalServerError;
import com.kuehne.nagel.coin.desk.util.Util;
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


public class BitCoinServiceImplTest extends Mockito {

    final String GET_CURRENT_RATE_URL = BitCoinServiceImpl.BASE_URL + "currentprice/INR.json";
    final String GET_HISTORICAL_URL = BitCoinServiceImpl.BASE_URL + "historical/close.json";
    private HttpClient httpClient;
    private boolean useDefaultHttpClient;

    @BeforeEach
    void setUp() {
        this.httpClient = createHttpClient();
        this.useDefaultHttpClient = Boolean.parseBoolean(System.getProperty("HttpClientTest.useDefaultHttpClient", "true"));
    }

    private HttpClient createHttpClient() {
        if (useDefaultHttpClient) {
            return HttpClient.newHttpClient();
        } else {
            return HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofMillis(1000))
                    .build();
        }
    }


    @Test
    void testGetHistoricalUrl() {
        LocalDate startDate = LocalDate.parse("2018-09-09");
        LocalDate endDate = LocalDate.parse("2018-10-09");
        assertEquals(GET_HISTORICAL_URL + "?start=" + startDate + "&end=" + endDate + "&currency=INR",
                (new BitCoinServiceImpl()).getHistoricalApiUrl("INR", startDate, endDate));
    }

    @Test
    void testCurrentPriceUrl() {
        assertEquals(GET_CURRENT_RATE_URL,
                (new BitCoinServiceImpl()).getCurrentPriceUrl("INR"));
    }

    @Test
    public void testGetCurrentRateByCurrency_when_success() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(GET_CURRENT_RATE_URL))
                .build();

        HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);

    }

    @Test

    public void testGetCurrentRate_when_no_currency_supported() {
        assertThrows(CurrencyNoSupportedException.class, () -> (new BitCoinServiceImpl()).getCurrentRateByCurrency("YYY"));
    }

    @Test
    public void testGetCurrentRateByCurrency_when_IO_Exception() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(BitCoinServiceImpl.BASE_URL + "/currentprice1/INR.json"))
                // HTTP method defaults to GET if not specified
                .build();

        HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 404);

    }

    @Test
    void testGetCurrentRateByCurrency_when_IllegalArgument_Exception() {
        assertThrows(IllegalArgumentException.class, () -> (new BitCoinServiceImpl())
                .getCurrentRateByCurrency("Exception occurred while getting current rate of currency:%s, cause:%s"));
    }

    @Test
    public void testGetHistoricalDetails_when_success() throws IOException, InterruptedException {
        LocalDate startDate = LocalDate.parse("2018-09-09");
        LocalDate endDate = LocalDate.parse("2018-10-09");

        var request = HttpRequest.newBuilder()
                .uri(URI.create(GET_HISTORICAL_URL + "?start=" + startDate + "&end=" + endDate + "&currency=INR"))
                // HTTP method defaults to GET if not specified
                .build();

        final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
    }


    @Test
    void testGetHistoricalDetails_when_Exception() {
        assertThrows(InternalServerError.class,
                () -> (new BitCoinServiceImpl()).getHistoricalDetails("XXX", null, null, 1));
        assertThrows(IllegalArgumentException.class, () -> (new BitCoinServiceImpl())
                .getHistoricalDetails("Exception occurred while getting current rate of currency:%s, cause:%s", null, null, 1));
    }

    @Test
    void testGetHighestRate_when_success() {
        CoinService coinService = new BitCoinServiceImpl();
        Map<String, Double> historicalMap = new TreeMap<>();
        IntStream stream = IntStream.range(1, 5);
        stream.forEach(i -> historicalMap.put(String.valueOf(i), Double.valueOf(i)));
        Optional<Double> lowestRate = coinService.getHighestRate(historicalMap);
        assertEquals(lowestRate, Optional.of(4.0));
    }

    @Test
    void testGetHighestRate_when_internal_server_error() {
        CoinService coinService = new BitCoinServiceImpl();
        Optional<Double> highestRate = coinService.getHighestRate(null);
        assertEquals(highestRate.isEmpty(), Boolean.TRUE);
    }

    @Test
    void testGetLowestRate_when_internal_server_error() {
        CoinService coinService = new BitCoinServiceImpl();
        Optional<Double> lowestRate = coinService.getLowestRate(null);
        assertEquals(lowestRate.isEmpty(), Boolean.TRUE);
    }

    @Test
    void testGetLowestRate_when_success() {
        CoinService coinService = new BitCoinServiceImpl();
        Map<String, Double> historicalMap = new TreeMap<>();
        IntStream stream = IntStream.range(1, 5);
        stream.forEach(i -> historicalMap.put(String.valueOf(i), Double.valueOf(i)));
        Optional<Double> lowestRate = coinService.getLowestRate(historicalMap);
        assertEquals(lowestRate, Optional.of(1.0));
    }

    @Test
    void testGetNoCurrencyFoundResponse() {
        assertEquals("Sorry, your requested currency GBP is not supported or is invalid",
                (new BitCoinServiceImpl()).getNoCurrencyFoundResponse("GBP"));
    }


    @Test
    void test_print_message() {
        final CoinService coinService = Mockito.spy(BitCoinServiceImpl.class);
        final LocalDate startDate = LocalDate.parse("2018-09-09");
        final LocalDate endDate = LocalDate.parse("2018-10-09");
        coinService.printMessage("INR", startDate, endDate, 98d, 50D, 100D, 90);
        verify(coinService, times(1)).printMessage("INR", startDate, endDate, 98d, 50D, 100D, 90);
    }

}

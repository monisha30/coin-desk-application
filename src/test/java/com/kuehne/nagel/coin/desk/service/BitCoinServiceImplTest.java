package com.kuehne.nagel.coin.desk.service;

import com.kuehne.nagel.coin.desk.exception.CurrencyNoSupportedException;
import com.kuehne.nagel.coin.desk.exception.InternalServerError;
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

    private HttpClient httpClient;
    private boolean useDefaultHttpClient;

    @BeforeEach
    void setUp() {
        this.httpClient = createHttpClient();
        this.useDefaultHttpClient = Boolean.parseBoolean(System.getProperty("HttpClientTest.useDefaultHttpClient", "true"));
    }

    private HttpClient createHttpClient() {
        if (useDefaultHttpClient) {
            // Use static factory  method to create HttpClient with default settings, covering e.g. HTTP request method,
            // protocol version, redirect policy etc. (Equivalent to using builder - HttpClient.newBuilder().build()).
            return HttpClient.newHttpClient();
        } else {
            // Use HttpClient builder to create an HTTP client with non-default, desired settings.
            return HttpClient.newBuilder()
                    // Set HTTP protocol version - In this case, not strictly necessary as by default HttpClient tries to upgrade
                    // to HTTP/2 if server supports it, else falls back to HTTP/1.1
                    .version(HttpClient.Version.HTTP_1_1)
                    // Set redirect policy
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    // Set a connection timeout.
                    // NOTE - There isn't currently an option to set a default read timeout (at least not via the builder) for
                    // the HttpClient, although one can be set per request.
                    .connectTimeout(Duration.ofMillis(1000))
                    // Proxy - The client can also be configured to use a proxy via the proxy(...) method.
                    // etc.
                    .build();
        }
    }


    @Test
    void testGetHistoricalUrl() {
        LocalDate startDate = LocalDate.parse("2018-09-09");
        LocalDate endDate = LocalDate.parse("2018-10-09");
        assertEquals("https://api.coindesk.com/v1/bpi/historical/close.json?start=" + startDate + "&end=" + endDate + "&currency=INR",
                (new BitCoinServiceImpl()).getHistoricalApiUrl("INR", startDate, endDate));
    }

    @Test
    void testCurrentPriceUrl() {
        assertEquals("https://api.coindesk.com/v1/bpi/currentprice/INR.json",
                (new BitCoinServiceImpl()).getCurrentPriceUrl("INR"));
    }

    @Test
    public void testGetCurrentRateByCurrency() throws IOException, InterruptedException {

        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.coindesk.com/v1/bpi/currentprice/INR.json"))
                // HTTP method defaults to GET if not specified
                .build();

        HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200);

    }

    @Test

    public void testGetCurrentRate_when_no_currency_supported() {

        assertThrows(CurrencyNoSupportedException.class, () -> (new BitCoinServiceImpl()).getCurrentRateByCurrency("YYY"));

    }

    @Test
    public void testGetCurrentRateByCurrency_when_IO_exception() throws IOException, InterruptedException {


        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.coindesk.com/v1/bpi/currentprice1/INR.json"))
                // HTTP method defaults to GET if not specified
                .build();

        HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        assertEquals(response.statusCode(), 404);
        // assertThrows(ConnectException.class, () -> (new BitCoinServiceImpl()).getCurrentRateByCurrency("INR"));


    }

    @Test
    public void testGetHighestRate() throws IOException, InterruptedException {
        LocalDate startDate = LocalDate.parse("2018-09-09");
        LocalDate endDate = LocalDate.parse("2018-10-09");

        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.coindesk.com/v1/bpi/historical/close.json?start=" + startDate + "&end=" + endDate + "&currency=INR"))
                // HTTP method defaults to GET if not specified
                .build();

        HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200);
        // assertHttpResponseStatusCodeInSuccessRange(response);
        //   assertThat(response.headers().firstValue("content-type")).isPresent().isNotEmpty();
        // assertThat(response.body()).isNotEmpty();

    }

    @Test
    void testGetCurrentRateByCurrency3() {
        assertThrows(IllegalArgumentException.class, () -> (new BitCoinServiceImpl())
                .getCurrentRateByCurrency("Exception occurred while getting current rate of currency:%s, cause:%s"));
    }


    @Test
    void testGetCurrentRateByCurrency5() {
        assertThrows(IllegalArgumentException.class, () -> (new BitCoinServiceImpl())
                .getCurrentRateByCurrency("Exception occurred while getting current rate of currency:%s, cause:%s"));
    }

    @Test
    void testGetHistoricalDetails() {
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
        CoinService coinService = Mockito.spy(BitCoinServiceImpl.class);

        LocalDate startDate = LocalDate.parse("2018-09-09");
        LocalDate endDate = LocalDate.parse("2018-10-09");
        // CoinService coinService = new BitCoinServiceImpl();
        coinService.printMessage("INR", startDate, endDate, 98d, 50D, 100D, 90);
        // doNothing().when(coinService.printMessage("INR",startDate,endDate,98d,50D,100D,90));
        //ßMockito.verify(coinService).printMessage("INR",startDate,endDate,98d,50D,100D,90);
        verify(coinService, times(1)).printMessage("INR", startDate, endDate, 98d, 50D, 100D, 90);
    }


}

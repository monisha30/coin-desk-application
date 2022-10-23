package com.kuehne.nagel.coin.desk.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class Util {


    public static String getApiResponse(final String url) throws IOException, InterruptedException {
        if(url == null || url.isEmpty()){
            throw new IllegalArgumentException("URL is not given");
        }
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
       return Optional.of(response.body()).orElseThrow(RuntimeException::new);
    }

    public static void validateCurrency(final String currency){
        if(currency ==null || currency.isBlank()){
            throw new IllegalArgumentException("Currency not passed in the argument");
        }
    }
}

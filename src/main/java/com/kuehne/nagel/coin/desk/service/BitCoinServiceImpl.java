package com.kuehne.nagel.coin.desk.service;

import com.google.gson.Gson;
import com.kuehne.nagel.coin.desk.exception.CurrencyNoSupportedException;
import com.kuehne.nagel.coin.desk.exception.HttpClientException;
import com.kuehne.nagel.coin.desk.exception.InternalServerError;
import com.kuehne.nagel.coin.desk.exception.NoRateAvailableException;
import com.kuehne.nagel.coin.desk.util.ErrorMessage;
import com.kuehne.nagel.coin.desk.util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BitCoinServiceImpl implements CoinService {

    static final String BASE_URL = "https://api.coindesk.com/v1/bpi/";
    static final String BRAND_POTENTIAL_INDEX = "bpi";

    final Logger LOG = Logger.getLogger(BitCoinServiceImpl.class.getName());

    @Override
    public Double getCurrentRateByCurrency(final String currency) {
        final String url = getCurrentPriceUrl(currency);
        final JSONObject currencyBpi = getResponse(url, currency).getJSONObject(currency.toUpperCase());
        try {
            @SuppressWarnings("unchecked") final TreeMap<String, Double> rateMap = new Gson().fromJson(String.valueOf(currencyBpi), TreeMap.class);
            return Optional.of(rateMap.get("rate_float")).orElseThrow();
        } catch (Exception e) {
            throw new InternalServerError(String.format(ErrorMessage.GET_CURRENT_RATE_ERROR.getDescription() , currency));
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Double> getHistoricalDetails(final String currency, final LocalDate startDate, final LocalDate endDate, final int duration) {
        final String url = getHistoricalApiUrl(currency, startDate, endDate);
        final JSONObject currencyBpi = getResponse(url, currency);
        try {
            return new Gson().fromJson(String.valueOf(currencyBpi), TreeMap.class);
        } catch (Exception e) {
            throw new InternalServerError(String.format("Exception occurred while getting historical rate of currency:%s, cause:%s", currency, e));
        }
    }

    private JSONObject getResponse(final String url, final String currency) {
        try {
            final String response = Util.getApiResponse(url);
            if(response !=null && response.trim().equalsIgnoreCase(getNoCurrencyFoundResponse(currency))){
                throw new CurrencyNoSupportedException("The requested currency " + currency + " is not supported.");
            }
            final JSONObject rateDetails = new JSONObject(response);
            final JSONObject currencyBpi = rateDetails.getJSONObject(BRAND_POTENTIAL_INDEX);
            return Optional.ofNullable(currencyBpi).orElseThrow();
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, String.format(ErrorMessage.RESPONSE_ERROR.getDescription(), url), e);
            Thread.currentThread().interrupt();
            throw new HttpClientException(String.format(ErrorMessage.RESPONSE_ERROR.getDescription(), url));
        } catch (IOException e) {
            throw new HttpClientException(String.format(ErrorMessage.RESPONSE_ERROR.getDescription(), url));
        } catch (JSONException e) {
            throw new InternalServerError(ErrorMessage.INTERNAL_SERVER_ERROR.getDescription());
        } catch (NoSuchElementException e) {
            throw new NoRateAvailableException(String.format(ErrorMessage.NO_RATE_ERROR.getDescription(), currency, url));
        }
    }

    @Override
    public Optional<Double> getHighestRate(final Map<String, Double> historicalRateMap) {
        if (historicalRateMap == null || historicalRateMap.isEmpty()) {
            return Optional.empty();
        }
        final Optional<Map.Entry<String, Double>> highestRate = historicalRateMap.entrySet().stream().min(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return highestRate.map(Map.Entry::getValue);

    }

    @Override
    public Optional<Double> getLowestRate(final Map<String, Double> historicalRateMap) {
        if (historicalRateMap == null || historicalRateMap.isEmpty()) {
            return Optional.empty();
        }
        Optional<Map.Entry<String, Double>> lowestRate = historicalRateMap.entrySet().stream().min(Map.Entry.comparingByValue());
        return lowestRate.map(Map.Entry::getValue);
    }


    public String getHistoricalApiUrl(final String currency, final LocalDate startDate, final LocalDate endDate) {
        return BASE_URL + "historical/close.json?start=" + startDate + "&end=" + endDate + "&currency=" + currency;
    }

    public String getCurrentPriceUrl(final String currency) {
        return BASE_URL + "currentprice/" + currency + ".json";
    }

    public String getNoCurrencyFoundResponse(final String currency){
        return "Sorry, your requested currency "+currency+" is not supported or is invalid";
    }
    @Override
    public void printMessage(final String currency, final LocalDate startDate, final LocalDate endDate, final Double currentRate, final Double lowestRate, final Double highestRate, final int duration) {
        final StringBuilder message = new StringBuilder();
        message.append(" -#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-# COIN DESK RESULT #-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-\n\n");
        message.append("::Inputted Values::\n");
        message.append(String.format("Currency: %s", currency) + "\n");
        message.append(String.format("Start Date: %s ", startDate) + "\n");
        message.append(String.format("End Date: %s", endDate) + "\n\n");

        message.append(String.format("The CURRENT Bitcoin rate: %s ", currentRate) + "\n");
        message.append(String.format("The LOWEST Bitcoin rate from %s to %s is : %s", startDate,endDate,lowestRate) + "\n");
        message.append(String.format("The HIGHEST Bitcoin rate from %s to %s is : %s", startDate, endDate,highestRate) + "\n\n");
        message.append("-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-# #-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-");
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.valueOf(message));
        }
    }
}

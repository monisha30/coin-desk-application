package com.kuehne.nagel.coin.desk.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface CoinService {

    String DATE_FORMATTER = "yyyy-MM-dd";

    Double getCurrentRateByCurrency(final String currency) ;

    Map<String, Double> getHistoricalDetails(final String currency, final LocalDate startDate, final LocalDate endDate, final int duration);

    Optional<Double> getHighestRate(final Map<String, Double> historicalDetailsMap);

    Optional<Double> getLowestRate(final Map<String, Double> historicalDetailsMap);

    void printMessage(final String currency, final LocalDate startDate, final LocalDate endDate, final Double currentRate, final Double lowestRate, final Double highestRate, final int duration);

    default void startProcess(String currency, final String startDateStr, final int duration) {
        currency = currency.toUpperCase();
        final Double currentRate = getCurrentRateByCurrency(currency);

        final DateTimeFormatter formatter = getDateTimeFormatter();
        final LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        final LocalDate endDate = startDate.plusDays(duration);

        /*Fetch the details only once, and use it to find highest and lowest rate*/
        final Map<String, Double> historicalMap = getHistoricalDetails(currency, startDate,endDate, duration);
        final Optional<Double> lowestRate = getLowestRate(historicalMap);
        if (lowestRate.isEmpty()) {
            throw new NoSuchElementException("Lowest rate not found");
        }

        final Optional<Double> highestRate = getHighestRate(historicalMap);
        if (highestRate.isEmpty()) {
            throw new NoSuchElementException("Highest rate not found");
        }

        printMessage(currency, startDate, endDate, currentRate, lowestRate.get(), highestRate.get(), duration);

    }

    default DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(DATE_FORMATTER);
    }


}

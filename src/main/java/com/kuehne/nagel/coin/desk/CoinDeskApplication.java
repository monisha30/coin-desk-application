package com.kuehne.nagel.coin.desk;

import com.kuehne.nagel.coin.desk.service.BitCoinServiceImpl;
import com.kuehne.nagel.coin.desk.util.Validator;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.logging.Logger;

import static com.kuehne.nagel.coin.desk.service.CoinService.DATE_FORMATTER;
import static java.util.logging.Level.SEVERE;

public class CoinDeskApplication {

    static final Logger LOG = Logger.getLogger(CoinDeskApplication.class.getName());

    public static void main(String[] args) {
        process();
    }

    public static void process() {
        try {
            final Scanner scanner = new Scanner(System.in);

            final String currency = getCurrency(scanner);
            final String startDate = getStartDate(scanner);
            int duration = getDuration(scanner);

            final BitCoinServiceImpl bitCoinService = new BitCoinServiceImpl();
            bitCoinService.startProcess(currency, startDate, duration);

        } catch (Exception e) {
            LOG.log(SEVERE, e.getMessage());
        }
    }

    private static int getDuration(Scanner scanner) {
        LOG.info("Enter duration in days ( E.g. 90): ");
        int duration = scanner.nextInt();
        Validator.validate(duration);
        return duration;
    }

    private static String getStartDate(Scanner scanner) {
        LOG.info("Enter start date (" + DATE_FORMATTER + "): ");
        String startDate = scanner.next();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        Validator.validateDate(startDate, formatter);
        return startDate;
    }

    private static String getCurrency(Scanner scanner) {
        LOG.info("Enter a currency code (E.g. USD): ");
        final String currency = scanner.next();
        if (currency == null || currency.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid currency");
        }
        return currency;
    }
}

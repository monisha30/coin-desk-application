package com.kuehne.nagel.coin.desk.exception;

public class CurrencyNoSupportedException extends RuntimeException{
    public CurrencyNoSupportedException(String message) {
        super(message);
    }
}
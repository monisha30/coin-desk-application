package com.kuehne.nagel.coin.desk.exception;

public class NoRateAvailableException extends RuntimeException{
    public NoRateAvailableException(String message) {
        super(message);
    }
}
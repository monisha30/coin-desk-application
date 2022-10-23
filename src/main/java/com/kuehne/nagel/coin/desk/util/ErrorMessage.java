package com.kuehne.nagel.coin.desk.util;

public enum ErrorMessage {
    RESPONSE_ERROR("Unable to get the response for Api"),
    INTERNAL_SERVER_ERROR("Server Error while fetching the response. Please check the input params."),
    NO_RATE_ERROR("No Rate available for currency %s using url %s"),
    GET_CURRENT_RATE_ERROR("Exception occurred while getting current rate of currency:%s");
    ;

     public String description;
    ErrorMessage(String description){
        this.description = description;
    }

}

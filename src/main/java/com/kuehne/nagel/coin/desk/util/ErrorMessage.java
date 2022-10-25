package com.kuehne.nagel.coin.desk.util;

public enum ErrorMessage {
    RESPONSE_ERROR("Unable to get the response for Api %s"),
    INTERNAL_SERVER_ERROR("Server Error while fetching the response. Please alter your start date and try again."),
    NO_RATE_ERROR("No Rate available for currency %s using url %s"),
    GET_CURRENT_RATE_ERROR("Exception occurred while getting current rate of currency:%s");
    
    private String description;

    ErrorMessage(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

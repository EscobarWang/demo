package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DailyForeignExchangeRatesDto {

    @JsonProperty("Date")
    private String date;
    @JsonProperty("USD/NTD")
    private String uSDToNTDRate;
    @JsonProperty("RMB/NTD")
    private String rMBToNTDRate;
    @JsonProperty("EUR/USD")
    private String eURToUSDRate;
    @JsonProperty("USD/JPY")
    private String uSDToJPYRate;
    @JsonProperty("GBP/USD")
    private String gBPToUSDRate;
    @JsonProperty("AUD/USD")
    private String aUDToUSDRate;
    @JsonProperty("USD/HKD")
    private String uSDToHKDRate;
    @JsonProperty("USD/RMB")
    private String uSDToRMBRate;
    @JsonProperty("USD/ZAR")
    private String uSDToZARRate;
    @JsonProperty("NZD/USD")
    private String nZDToUSDRate;
}

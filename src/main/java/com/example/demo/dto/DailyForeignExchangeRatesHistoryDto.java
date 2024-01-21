package com.example.demo.dto;

import lombok.Data;

@Data
public class DailyForeignExchangeRatesHistoryDto {

    // 起始日期
    private String startDate;
    // 結束日期
    private String endDate;
    // 幣別
    private String currency1;
}

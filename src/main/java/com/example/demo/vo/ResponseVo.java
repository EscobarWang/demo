package com.example.demo.vo;

import lombok.Data;

import java.util.List;

@Data
public class ResponseVo {

    private ErrorCodeVo error;
    private List<DailyForeignExchangeRatesHistoryVo> currency;
}

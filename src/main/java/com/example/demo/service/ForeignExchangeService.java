package com.example.demo.service;

import com.example.demo.dto.DailyForeignExchangeRatesHistoryDto;
import com.example.demo.vo.ResponseVo;
import org.springframework.stereotype.Service;

import java.text.ParseException;

public interface ForeignExchangeService {

    /**
     * 取得美元/台幣的歷史資料
     * @param dailyForeignExchangeRatesHistoryDto 查詢參數條件
     * @return ResponseVo
     * @throws ParseException 日期解析異常
     */
    ResponseVo getDailyForeignExchangeRatesHistory(DailyForeignExchangeRatesHistoryDto dailyForeignExchangeRatesHistoryDto)throws ParseException ;
}

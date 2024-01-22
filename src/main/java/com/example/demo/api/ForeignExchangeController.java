package com.example.demo.api;

import com.example.demo.dto.DailyForeignExchangeRatesHistoryDto;
import com.example.demo.service.ForeignExchangeService;
import com.example.demo.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class ForeignExchangeController {

    @Autowired
    ForeignExchangeService foreignExchangeService;

    @GetMapping("/getDailyForeignExchangeRates")
    @Scheduled(cron = "0 43 20 * * ?", zone = "Asia/Taipei")
    public void getDailyForeignExchangeRates() {
        foreignExchangeService.getDailyForeignExchangeRates(null);
    }

    @PostMapping("/getDailyForeignExchangeRatesHistory")
    public ResponseVo getDailyForeignExchangeRatesHistory(@RequestBody DailyForeignExchangeRatesHistoryDto dailyForeignExchangeRatesHistoryDto) throws ParseException {
        return foreignExchangeService.getDailyForeignExchangeRatesHistory(dailyForeignExchangeRatesHistoryDto);
    }
}

package com.example.demo.api;

import com.example.demo.dto.DailyForeignExchangeRatesHistoryDto;
import com.example.demo.service.ForeignExchangeService;
import com.example.demo.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class ForeignExchangeController {

    @Autowired
    ForeignExchangeService foreignExchangeService;

    //透過 @RequestMapping 指定從/會被對應到此hello()方法
    @PostMapping("/getDailyForeignExchangeRatesHistory")
    public ResponseVo hello(@RequestBody DailyForeignExchangeRatesHistoryDto dailyForeignExchangeRatesHistoryDto) throws ParseException {
        return foreignExchangeService.getDailyForeignExchangeRatesHistory(dailyForeignExchangeRatesHistoryDto);
    }
}

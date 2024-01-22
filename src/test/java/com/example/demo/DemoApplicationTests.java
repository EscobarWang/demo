package com.example.demo;

import com.example.demo.dto.DailyForeignExchangeRatesHistoryDto;
import com.example.demo.service.ForeignExchangeService;
import com.example.demo.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootTest
@Slf4j
class DemoApplicationTests {

    @Autowired(required = false)
    DataSource dataSource;
    @Autowired
    ForeignExchangeService foreignExchangeService;

    @Test
    void test() throws ParseException {
        SimpleDateFormat dtf = new SimpleDateFormat("yyyyMMdd");
        // 過去時間(假日):2024013(測試時間2024/1/19)，預測結果:今日無外匯成交資料
        String testTime1 = "20240113";
        log.info("日期:20240113");
        foreignExchangeService.getDailyForeignExchangeRates(dtf.parse(testTime1));
        // 未來時間:20240225(測試時間2024/1/19)，預測結果:今日無外匯成交資料
        String testTime2 = "20240225";
        log.info("日期:20240225");
        foreignExchangeService.getDailyForeignExchangeRates(dtf.parse(testTime2));
        // 系統時間:20240119(測試時間2024/1/19)，預測結果:新增外匯成交資料成功
        String testTime3 = "20240119";
        log.info("日期:20240119");
        foreignExchangeService.getDailyForeignExchangeRates(dtf.parse(testTime3));
    }

    @Test
    void test1() throws ParseException {
        // 測試1:起始日期早於1年前(測試時間2024/1/21)，預測結果:日期區間不符
        DailyForeignExchangeRatesHistoryDto dto1 = new DailyForeignExchangeRatesHistoryDto();
        dto1.setCurrency1("USD");
        dto1.setStartDate("2023/01/20");
        dto1.setEndDate("2024/01/19");
        ResponseVo dailyForeignExchangeRatesHistory = foreignExchangeService.getDailyForeignExchangeRatesHistory(dto1);
        log.info("測試1:{}", dailyForeignExchangeRatesHistory);

        // 測試2:起始日期晚於結束日期(測試時間2024/1/21)，預測結果:日期區間不符
        DailyForeignExchangeRatesHistoryDto dto2 = new DailyForeignExchangeRatesHistoryDto();
        dto2.setCurrency1("USD");
        dto2.setStartDate("2024/01/20");
        dto2.setEndDate("2024/01/19");
        ResponseVo dailyForeignExchangeRatesHistory2 = foreignExchangeService.getDailyForeignExchangeRatesHistory(dto2);
        log.info("測試2:{}", dailyForeignExchangeRatesHistory2);

        // 測試3:結束日期=系統日(測試時間2024/1/21)，預測結果:日期區間不符
        DailyForeignExchangeRatesHistoryDto dto3 = new DailyForeignExchangeRatesHistoryDto();
        dto3.setCurrency1("USD");
        dto3.setStartDate("2024/01/19");
        dto3.setEndDate("2024/01/21");
        ResponseVo dailyForeignExchangeRatesHistory3 = foreignExchangeService.getDailyForeignExchangeRatesHistory(dto3);
        log.info("測試3:{}", dailyForeignExchangeRatesHistory3);

        // 測試4:起始日期=1年前到結束日期=系統日-1(測試時間2024/1/21)，預測結果:回傳區間範圍資料
        DailyForeignExchangeRatesHistoryDto dto4 = new DailyForeignExchangeRatesHistoryDto();
        dto4.setCurrency1("USD");
        dto4.setStartDate("2023/01/21");
        dto4.setEndDate("2024/01/20");
        ResponseVo dailyForeignExchangeRatesHistory4 = foreignExchangeService.getDailyForeignExchangeRatesHistory(dto4);
        log.info("測試4:{}", dailyForeignExchangeRatesHistory4.getCurrency());
    }
}
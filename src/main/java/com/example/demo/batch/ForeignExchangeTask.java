package com.example.demo.batch;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.dto.DailyForeignExchangeRatesDto;
import com.example.demo.util.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ForeignExchangeTask {

    @Autowired
    DataSource dataSource;

    // 測試用需注解
//    @Scheduled(cron = "0 14 12 * * ?", zone="Asia/Taipei")

    public void getDailyForeignExchangeRates(Date date) {
        log.info("取得外匯成交資料排程開始");
        SimpleDateFormat dtf = new SimpleDateFormat("yyyyMMdd");
        String now;
        if (Objects.isNull(date)) {
            // 獲取今日日期(字串)
            now = dtf.format(Calendar.getInstance().getTime());
        } else {
            // 測試用
            now = dtf.format(date);
        }


        // 取得外匯成交資料
        String responseString = HttpService.getInstance().doGet("https://openapi.taifex.com.tw/v1/DailyForeignExchangeRates");
        List<DailyForeignExchangeRatesDto> dailyForeignExchangeRatesDtoList = JSONArray.parseArray(responseString, DailyForeignExchangeRatesDto.class);

        // 篩選出今日外匯成交資料
        dailyForeignExchangeRatesDtoList = dailyForeignExchangeRatesDtoList.stream().filter(item -> StringUtils.equals(now, item.getDate())).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(dailyForeignExchangeRatesDtoList)) {
            log.info("今日無外匯成交資料");
        } else {
            // 新增語法
            String insertSql = "INSERT INTO DAILY_FOREIGN_EXCHANGES_RATES (DATE,USD_TO_NTD_RATE,CREATE_DATE) VALUES (?,?,?)";
            try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                connection.setAutoCommit(false);
                for (DailyForeignExchangeRatesDto dailyForeignExchangeRatesDto : dailyForeignExchangeRatesDtoList) {
                    preparedStatement.setString(1, dailyForeignExchangeRatesDto.getDate());
                    preparedStatement.setString(2, dailyForeignExchangeRatesDto.getUSDToNTDRate());
                    preparedStatement.setTimestamp(3, new java.sql.Timestamp((new Date().getTime())));
                    log.info("新增今日外匯成交資料,日期:{},美元/台幣:{}", dailyForeignExchangeRatesDto.getDate(), dailyForeignExchangeRatesDto.getUSDToNTDRate());
                    preparedStatement.executeUpdate();
                }
                connection.commit();
                log.info("新增完成");
            } catch (Exception e) {
                log.error("發生錯誤:", e);
                e.printStackTrace();
            }
        }
        log.info("取得外匯成交資料排程結束");
    }

}

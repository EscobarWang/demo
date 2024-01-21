package com.example.demo.service.impl;

import com.example.demo.dto.DailyForeignExchangeRatesHistoryDto;
import com.example.demo.service.ForeignExchangeService;
import com.example.demo.vo.DailyForeignExchangeRatesHistoryVo;
import com.example.demo.vo.ErrorCodeVo;
import com.example.demo.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class ForeignExchangeServiceImpl implements ForeignExchangeService {

    @Autowired(required = false)
    DataSource dataSource;

    @Override
    public ResponseVo getDailyForeignExchangeRatesHistory(DailyForeignExchangeRatesHistoryDto dailyForeignExchangeRatesHistoryDto) throws ParseException {
        ResponseVo responseVo = new ResponseVo();
        ErrorCodeVo errorCodeVo = new ErrorCodeVo();
        List<DailyForeignExchangeRatesHistoryVo> dailyForeignExchangeRatesHistoryVoList = new ArrayList<>();

        SimpleDateFormat dtf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat slashDtf = new SimpleDateFormat("yyyy/MM/dd");

        if (Objects.isNull(dailyForeignExchangeRatesHistoryDto.getStartDate()) || Objects.isNull(dailyForeignExchangeRatesHistoryDto.getEndDate())) {
            errorCodeVo.setCode("E001");
            errorCodeVo.setMessage("日期區間不符");
            responseVo.setError(errorCodeVo);
            return responseVo;
        }
        Date startDate = slashDtf.parse(dailyForeignExchangeRatesHistoryDto.getStartDate());
        Date endDate = slashDtf.parse(dailyForeignExchangeRatesHistoryDto.getEndDate());
        Date now = new Date();
        // 宣告變數:今日
        Calendar instance = Calendar.getInstance();
        instance.setTime(now);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        // 宣告變數:去年
        Calendar lastYear = Calendar.getInstance();
        lastYear.setTime(now);
        lastYear.set(Calendar.HOUR_OF_DAY, 0);
        lastYear.set(Calendar.MINUTE, 0);
        lastYear.set(Calendar.SECOND, 0);
        lastYear.set(Calendar.MILLISECOND, 0);
        lastYear.add(Calendar.YEAR, -1);

        // 驗證起始日期不超過1年前
        if (startDate.compareTo(lastYear.getTime()) < 0) {
            errorCodeVo.setCode("E001");
            errorCodeVo.setMessage("日期區間不符");
            responseVo.setError(errorCodeVo);
            return responseVo;
        }
        // 驗證起始日期在結束日期之前
        if (endDate.compareTo(startDate) < 0) {
            errorCodeVo.setCode("E001");
            errorCodeVo.setMessage("日期區間不符");
            responseVo.setError(errorCodeVo);
            return responseVo;
        }
        // 驗證結束日期在今天以前
        if (endDate.compareTo(instance.getTime()) >= 0) {
            errorCodeVo.setCode("E001");
            errorCodeVo.setMessage("日期區間不符");
            responseVo.setError(errorCodeVo);
            return responseVo;
        }

        // 新增語法
        String insertSql = "SELECT DATE,USD_TO_NTD_RATE FROM DAILY_FOREIGN_EXCHANGES_RATES WHERE DATE BETWEEN ? AND ?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
            preparedStatement.setString(1, dtf.format(startDate));
            preparedStatement.setString(2, dtf.format(endDate));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                DailyForeignExchangeRatesHistoryVo dailyForeignExchangeRatesHistoryVo = new DailyForeignExchangeRatesHistoryVo();
                dailyForeignExchangeRatesHistoryVo.setDate(resultSet.getString(1));
                dailyForeignExchangeRatesHistoryVo.setUsd(resultSet.getString(2));
                dailyForeignExchangeRatesHistoryVoList.add(dailyForeignExchangeRatesHistoryVo);
            }
            log.info("查詢完成");
            errorCodeVo.setCode("E000");
            errorCodeVo.setMessage("成功");
            responseVo.setError(errorCodeVo);
            responseVo.setCurrency(dailyForeignExchangeRatesHistoryVoList);
            return responseVo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

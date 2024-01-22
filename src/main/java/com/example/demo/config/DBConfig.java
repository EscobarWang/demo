package com.example.demo.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    @Bean
    @Primary
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:sqlserver://localhost\\SQLEXPRESS:1433;SelectMethod=cursor;database=TAIFEXDB;")
                .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .username("sa")
                .password("123456")
                .build();
    }

}

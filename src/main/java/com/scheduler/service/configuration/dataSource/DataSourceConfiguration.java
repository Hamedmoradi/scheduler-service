package com.scheduler.service.configuration.dataSource;

import lombok.RequiredArgsConstructor;
import oracle.jdbc.datasource.impl.OracleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfiguration {
    @Value("${spring.scheduler-datasource.username}")
    private  String user;
    @Value("${spring.scheduler-datasource.password}")
    private  String password;
    @Value("${spring.scheduler-datasource.jdbcUrl}")
    private  String url;
    @Bean
    public DataSource getDataSource() {

        OracleDataSource dataSource = null;
        try {
            dataSource = new OracleDataSource();
            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setURL(url);
            dataSource.setImplicitCachingEnabled(true);
            return dataSource;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}


package ir.baam.config.dataSource;


import oracle.jdbc.datasource.impl.OracleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DataSourceConfiguration {

    @Bean
    public DataSource getDataSource() {

        OracleDataSource dataSource = null;
        try {
            dataSource = new OracleDataSource();
            dataSource.setUser("MT");
            dataSource.setPassword("kjio0988");
            dataSource.setURL("jdbc:oracle:thin:@//172.30.41.22:1521/offpayapdb1");
            dataSource.setImplicitCachingEnabled(true);
            return dataSource;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //todo config hikari
}

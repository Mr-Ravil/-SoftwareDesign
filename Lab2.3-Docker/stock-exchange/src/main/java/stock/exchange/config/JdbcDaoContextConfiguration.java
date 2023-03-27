package stock.exchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import stock.exchange.dao.StockJdbcDao;

import javax.sql.DataSource;

//@Configuration
public class JdbcDaoContextConfiguration {
    @Bean
    public StockJdbcDao stockJdbcDao(DataSource dataSource) {
        return new StockJdbcDao(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:stock.db");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }
}

package config;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import dao.EventDao;
import dao.EventJdbcDao;

import javax.sql.DataSource;

public class JdbcDaoContextConfiguration {
    public EventDao eventDao(DataSource dataSource) {
        return new EventJdbcDao(dataSource);
    }

    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:EventSourcing.db");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }
}

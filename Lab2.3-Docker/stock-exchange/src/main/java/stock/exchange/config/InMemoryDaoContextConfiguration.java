package stock.exchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import stock.exchange.dao.StockDao;
import stock.exchange.dao.StockInMemoryDao;

@Configuration
public class InMemoryDaoContextConfiguration {
    @Bean
    public StockDao stockInMemoryDao() {
        return new StockInMemoryDao();
    }
}

package user.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import user.account.dao.HttpStockDao;

@Configuration
public class HttpDaoContextConfiguration {
    @Bean
    public HttpStockDao stockInMemoryDao() {
        return new HttpStockDao("http://localhost:8080");
    }

}

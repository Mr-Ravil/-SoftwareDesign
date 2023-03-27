package user.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import user.account.dao.UserDao;
import user.account.dao.UserInMemoryDao;

@Configuration
public class InMemoryDaoContextConfiguration {
    @Bean
    public UserDao userController() {
        return new UserInMemoryDao();
    }
}

package user.account.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import user.account.model.Portfolio;
import user.account.model.User;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class UserJdbcDao extends JdbcDaoSupport implements UserDao {
    public UserJdbcDao(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }


    @Override
    public int addUser(User user) {
        return 0;
    }

    @Override
    public void addBalance(int id, long balance) {

    }

    @Override
    public Optional<User> getUser(int id) {
        return Optional.empty();
    }

    @Override
    public List<Portfolio> getPortfoliosByUserId(int id) {
        return null;
    }
}

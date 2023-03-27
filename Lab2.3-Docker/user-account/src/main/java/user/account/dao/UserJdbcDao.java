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
        String sql = "INSERT INTO User (name, balance) VALUES (?, ?)";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().update(sql, user.getName(), user.getBalance());
    }

    @Override
    public void changeBalance(int userId, long balance) {
        String sql = "UPDATE User SET balance = balance + ? WHERE id = ? and balance + ? >= 0";
        assert getJdbcTemplate() != null;
        if (getJdbcTemplate().update(sql, balance, userId, balance) == 0) {
            throw new AssertionError("Negative balance");
        }
    }

    @Override
    public long getBalance(int userId) {
        String sql = "SELECT balance FROM User WHERE userId = ?";
        assert getJdbcTemplate() != null;

        Optional<Long> optionalBalance =
                getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Long.class), userId).stream().findFirst();

        if (optionalBalance.isEmpty()) {
            throw new AssertionError("There are no such users");
        }

        return optionalBalance.get();
    }

    @Override
    public User getUser(int id) {
        String sql = "SELECT * FROM User WHERE userId = ?";
        assert getJdbcTemplate() != null;

        Optional<User> optionalUser =
                getJdbcTemplate().query(sql, new BeanPropertyRowMapper(User.class), id).stream().findFirst();

        if (optionalUser.isEmpty()) {
            throw new AssertionError("There are no such users");
        }

        return optionalUser.get();
    }

    @Override
    public void updatePortfolio(Portfolio portfolio) {
//        String sql2 = "INSERT INTO Portfolio (userId, stockId, amount) VALUES (?, ?, ?)";
        if (portfolio.getAmount() >= 0) {
            String sql = "MERGE INTO Portfolio AS t" +
                    "USING (" +
                    "    VALUES  (?, ?, ?)," +
                    "       ) AS s (userId, stockId, amount)" +
                    "    ON t.userId = s.userId and t.stockId = s.stockId " +
                    "    WHEN MATCHED THEN" +
                    "        UPDATE SET t.amount=t.amount + s.amount" +
                    "    WHEN NOT MATCHED THEN" +
                    "        INSERT (userId, stockId, amount)" +
                    "              VALUES (s.userId, s.stockId, s.amount)";
            assert getJdbcTemplate() != null;
            getJdbcTemplate().update(sql, portfolio.getUserId(), portfolio.getStockId(), portfolio.getAmount());
        } else {
            String sql = "UPDATE Portfolio SET amount = amount + ? WHERE userId = ? and stockId = ? and amount + ? >= 0";
            assert getJdbcTemplate() != null;
            if (getJdbcTemplate().update(sql, portfolio.getAmount(), portfolio.getUserId(), portfolio.getAmount()) == 0) {
                throw new AssertionError("Negative amount");
            }
        }
    }

    @Override
    public List<Portfolio> getPortfoliosByUserId(int userId) {
        String sql = "SELECT * FROM Portfolio WHERE userId = ?";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Portfolio.class), userId);
    }
}

package stock.exchange.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import stock.exchange.model.Stock;

import javax.sql.DataSource;
import java.util.Optional;

public class StockJdbcDao extends JdbcDaoSupport implements StockDao {
    public StockJdbcDao(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }

    @Override
    public int addStock(Stock stock) {
        String sql = "INSERT INTO Stock (companyName, price, amount) VALUES (?, ?, ?)";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().update(sql, stock.getCompanyName(), stock.getPrice(), stock.getAmount());
    }

    @Override
    public void changeAmount(int stockId, long amount) {
        String sql = "UPDATE Stock SET amount = amount + ? WHERE id = ? and amount + ? >= 0";
        assert getJdbcTemplate() != null;
        if (getJdbcTemplate().update(sql, amount, stockId, amount) == 0) {
            throw new AssertionError("Not enough stocks");
        }
    }

    @Override
    public void setPrice(int stockId, long newPrice) {
        String sql = "UPDATE Stock SET price = ? WHERE id = ?";
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(sql, newPrice, stockId);
    }

    @Override
    public Stock getStock(int id) {
        String sql = "SELECT * FROM Stock WHERE id = ?";
        assert getJdbcTemplate() != null;

        Optional<Stock> optionalStock =
                getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Stock.class), id).stream().findFirst();

        if (optionalStock.isEmpty()) {
            throw new AssertionError("There are no such stocks");
        }

        return optionalStock.get();
    }
}

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
    public void addAmount(int id, long amount) {
        String sql = "UPDATE Stock SET amount = amount + ? WHERE id = ?";
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(sql, amount, id);
    }

    @Override
    public void changePrice(int id, long price) {
        String sql = "UPDATE Stock SET price = ? WHERE id = ?";
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(sql, price, id);
    }

    @Override
    public Optional<Stock> getStock(int id) {
        String sql = "SELECT * FROM Stock WHERE id = ?";
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Stock.class), id).stream().findFirst();
    }
}

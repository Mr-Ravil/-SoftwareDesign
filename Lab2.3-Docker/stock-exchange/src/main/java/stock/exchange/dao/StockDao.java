package stock.exchange.dao;

import stock.exchange.model.Stock;

import java.util.Optional;

public interface StockDao {

    int addStock(Stock stock);

    void addAmount(int id, long amount);

    void changePrice(int id, long price);

    Optional<Stock> getStock(int id);

}

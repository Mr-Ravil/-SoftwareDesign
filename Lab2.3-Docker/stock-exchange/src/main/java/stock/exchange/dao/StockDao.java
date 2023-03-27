package stock.exchange.dao;

import stock.exchange.model.Stock;

public interface StockDao {

    int addStock(Stock stock);

    void changeAmount(int stockId, long amount);

    void setPrice(int stockId, long newPrice);

    Stock getStock(int id);

}

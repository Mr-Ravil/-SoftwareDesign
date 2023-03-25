package user.account.dao;


import user.account.model.Portfolio;

import java.util.List;

public class HttpStockDao implements StockDao {

    @Override
    public long getPrice(int stockId) {
        return 0;
    }

    @Override
    public long getTotalBalance(List<Portfolio> portfolios) {
        return 0;
    }

    @Override
    public void buy(int userId, int stockId, long amount) {

    }

    @Override
    public void sell(int userId, int stockId, long amount) {

    }
}

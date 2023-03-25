package user.account.dao;


import user.account.model.Portfolio;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

public interface StockDao {

    long getPrice(int stockId);

    long getTotalBalance(List<Portfolio> portfolios);

    void buy(int userId, int stockId, long amount);

    void sell(int userId, int stockId, long amount);

}

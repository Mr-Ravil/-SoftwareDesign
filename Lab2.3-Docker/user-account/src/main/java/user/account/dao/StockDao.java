package user.account.dao;


import user.account.model.Portfolio;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface StockDao {

    long getPrice(long stockId);

    long buy(int stockId, long amount) throws IOException, URISyntaxException, InterruptedException;

    long sell(int stockId, long amount) throws IOException, URISyntaxException, InterruptedException;

}

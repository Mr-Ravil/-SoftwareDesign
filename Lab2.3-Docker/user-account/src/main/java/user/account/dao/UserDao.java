package user.account.dao;

import user.account.model.Portfolio;
import user.account.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    int addUser(User user);

    void addBalance(int id, long balance);

    Optional<User> getUser(int id);

    List<Portfolio> getPortfoliosByUserId(int id);

}

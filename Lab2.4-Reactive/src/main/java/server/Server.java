package server;

import dao.MongoDao;
import io.reactivex.netty.protocol.http.server.HttpServer;
import model.Currency;
import model.Product;
import model.User;
import rx.Observable;

import java.util.List;
import java.util.Map;

public class Server {
    private static final String PAGE_NOT_FOUND = "Page not found";
    private static final String REGISTER_ERROR = "Register error";
    private static final String ADD_PRODUCT_ERROR = "Add product error";
    private static final String NO_SUCH_USER = "No such user";
    private final int port;
    private final MongoDao dao;

    public Server(int port) {
        this.port = port;
        dao = new MongoDao();
    }

    public void start() {
        HttpServer
                .newServer(port)
                .start((req, resp) -> {

                    String path = req.getDecodedPath().substring(1);
                    Map<String, List<String>> params = req.getQueryParameters();

                    Observable<String> response = switch (path) {
                        case "register" -> register(params);
                        case "add_product" -> addProduct(params);
                        case "products" -> getProducts(params);
                        default -> Observable.just(PAGE_NOT_FOUND);
                    };

                    return resp.writeString(response);
                })
                .awaitShutdown();
    }

    private Observable<String> register(Map<String, List<String>> params) {
        int id = Integer.parseInt(params.get("id").get(0));
        Currency currency = Currency.valueOf(params.get("cur").get(0));
        return dao.addUser(new User(id, currency))
                .map(ignore -> "Created user with id=" + id + " and currency=" + currency)
                .onErrorReturn(error -> {
                    error.printStackTrace();
                    return REGISTER_ERROR;
                });
    }

    private Observable<String> addProduct(Map<String, List<String>> params) {
        String name = params.get("name").get(0);
        double price = Double.parseDouble(params.get("price").get(0));
        Currency currency = Currency.valueOf(params.getOrDefault("cur", List.of("USD")).get(0));
        return dao.addProduct(new Product(name, price, currency))
                .map(ignore -> "Added product with name=" + name
                        + " and " + currency.getCurrencyCode() + " price=" + price)
                .onErrorReturn(error -> {
                    error.printStackTrace();
                    return ADD_PRODUCT_ERROR;
                });
    }

    private Observable<String> getProducts(Map<String, List<String>> params) {
        int uid = Integer.parseInt(params.get("uid").get(0));
        return dao.getUser(uid).flatMap(user ->
                dao.getProducts().map(product -> product.toString(user.getCurrency()) + "\n")
        ).onErrorReturn(error -> {
            error.printStackTrace();
            return NO_SUCH_USER;
        });
    }
}

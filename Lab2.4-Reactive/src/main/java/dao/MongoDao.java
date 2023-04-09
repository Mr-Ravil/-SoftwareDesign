package dao;

import com.mongodb.rx.client.*;
import model.Product;
import model.User;
import org.bson.Document;
import rx.Observable;

import static com.mongodb.client.model.Filters.eq;

public class MongoDao {
    private static final int DAO_PORT = 27017;
    private static final String DATABASE_NAME = "reactive";
    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection<Document> users;
    private final MongoCollection<Document> products;

    public MongoDao() {
        client = createMongoClient(DAO_PORT);
        database = client.getDatabase(DATABASE_NAME);
        users = database.getCollection("users");
        products = database.getCollection("products");
    }

    public Observable<Success> addUser(User user) {
        return users.insertOne(user.toDocument());
    }

    public Observable<User> getUser(int id) {
        return users.find(eq("_id", id)).first().map(User::new);
    }

    public Observable<Success> addProduct(Product product) {
        return products.insertOne(product.toDocument());
    }

    public Observable<Product> getProducts() {
        return products.find().toObservable().map(Product::new);
    }

    private static MongoClient createMongoClient(int port) {
        return MongoClients.create("mongodb://localhost:" + port);
    }
}

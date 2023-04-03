package actors;

import akka.actor.UntypedActor;
import com.google.gson.Gson;
import model.Request;
import model.Response;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ChildActor extends UntypedActor {
    private final String host;

    public ChildActor(String host) {
        this.host = host;
    }


    @Override
    public void onReceive(Object message) throws Throwable, Throwable {
        if (message instanceof Request request) {
            String encQuery = URLEncoder.encode(request.query, StandardCharsets.UTF_8);
            URI uri = URI.create(host + "?top=" + request.top + "&q=" + encQuery);

            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).build();

            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            Response response = new Gson().fromJson(httpResponse.body(), Response.class);
            getSender().tell(response, getSelf());
        }
    }
}

package stub;

import com.google.gson.Gson;
import model.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StubServerTests {
    private static final int PORT = 8080;
    private static final String HOST = "http://localhost:" + PORT + "/";

    @Test
    public void oneHostTest() throws IOException, InterruptedException {
        String host = "google";
        String query = "super query";
        String encQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        int top = 5;
        URI uri = URI.create(HOST + host + "?top=" +top+"&q=" + encQuery);


        Response actualResponse;
        try (StubServer stubServer = new StubServer(PORT, List.of(host))) {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            actualResponse = new Gson().fromJson(httpResponse.body(), Response.class);
        }

        Response expectedResponse = StubServer.createResponse(host, query, top);

        assertEquals(expectedResponse.result, actualResponse.result);
    }

    @Test
    public void severalHostsTest() throws IOException, InterruptedException {
        List<String> hosts = List.of("google", "yandex", "bing");

        try (StubServer stubServer = new StubServer(PORT, hosts)) {
            for (String host : hosts) {
                String query = "super query";
                String encQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
                int top = 5;

                URI uri = URI.create(HOST + host + "?top=" +top+"&q=" + encQuery);

                HttpClient client = HttpClient.newBuilder().build();
                HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
                HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());


                Response actualResponse = new Gson().fromJson(httpResponse.body(), Response.class);

                Response expectedResponse = StubServer.createResponse(host, query, top);
                assertEquals(expectedResponse.result, actualResponse.result);
            }
        }

    }
}

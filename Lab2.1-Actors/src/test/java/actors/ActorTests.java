package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import gsons.Request;
import gsons.Response;
import stub.StubServer;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toCollection;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


public class ActorTests {
    private static final Duration defaultTimeout = Duration.ofMillis(500);
    private static final Duration askTimeout = Duration.ofSeconds(20);
    private static final int CLIENT_PORT = 8080;
    private static final int PORT1 = 8088;
    private static final int PORT2 = 8087;
    private static final int TOP = 5;
    private static final String HOST = "http://localhost:";

    private static String getHost(int port, String searcher) {
        return HOST + port + "/" + searcher;
    }

    @Test
    public void oneHostTest() {
        String searcher = "google";
        String host = getHost(PORT1, searcher);
        String query = "super query";

        try (StubServer stubServer = new StubServer(PORT1, List.of(searcher))) {
            try (StubServer clientServer = new StubServer(CLIENT_PORT)) {
                ActorSystem system = ActorSystem.create("TestSystem");

                ActorRef master = system.actorOf(
                        Props.create(MasterActor.class, List.of(host), defaultTimeout), "master"
                );

                Response expectedResponse = StubServer.createResponse(searcher, query, TOP);

                Object message = PatternsCS.ask(master, new Request(TOP, query), askTimeout.toMillis())
                        .toCompletableFuture().join();

                assertTrue(message instanceof Map);
                Map<String, Response> actualResponses = (Map<String, Response>) message;

                assertEquals(1, actualResponses.size());

                for (Map.Entry<String, Response> entry : actualResponses.entrySet()) {
                    assertEquals(searcher, entry.getKey());
                    assertEquals(expectedResponse, entry.getValue());
                }
            }
        }
    }


    @Test
    public void severalHostsTest() {
        List<String> searchers = List.of("google", "yandex", "bing");
        List<String> hosts = searchers.stream().map(searcher -> getHost(PORT1, searcher)).toList();
        String query = "super query";

        try (StubServer stubServer = new StubServer(PORT1, searchers)) {
            try (StubServer clientServer = new StubServer(CLIENT_PORT)) {
                ActorSystem system = ActorSystem.create("TestSystem");

                ActorRef master = system.actorOf(
                        Props.create(MasterActor.class, hosts, defaultTimeout), "master"
                );

                Object message = PatternsCS.ask(master, new Request(TOP, query), askTimeout.toMillis())
                        .toCompletableFuture().join();

                assertTrue(message instanceof Map);
                Map<String, Response> actualResponses = (Map<String, Response>) message;

                assertEquals(searchers.size(), actualResponses.size());

                for (Map.Entry<String, Response> entry : actualResponses.entrySet()) {
                    assertTrue(searchers.contains(entry.getKey()));
                    assertEquals(StubServer.createResponse(entry.getKey(), query, TOP), entry.getValue());
                }
            }
        }
    }

    @Test
    public void oneSlowHostTest() {
        String searcher = "google";
        String host = getHost(PORT1, searcher);
        String query = "super query";

        try (StubServer stubServer = new StubServer(PORT1, List.of(searcher), defaultTimeout.plusSeconds(1))) {
            try (StubServer clientServer = new StubServer(CLIENT_PORT)) {
                ActorSystem system = ActorSystem.create("TestSystem");

                ActorRef master = system.actorOf(
                        Props.create(MasterActor.class, List.of(host), defaultTimeout), "master"
                );

                Response expectedResponse = StubServer.createResponse(searcher, query, TOP);

                Object message = PatternsCS.ask(master, new Request(TOP, query), askTimeout.toMillis())
                        .toCompletableFuture().join();

                assertTrue(message instanceof Map);
                Map<String, Response> actualResponses = (Map<String, Response>) message;

                assertEquals(0, actualResponses.size());

                for (Map.Entry<String, Response> entry : actualResponses.entrySet()) {
                    assertEquals(searcher, entry.getKey());
                    assertEquals(expectedResponse, entry.getValue());
                }
            }
        }
    }

    @Test
    public void oneSlowTwoFastHostsTest() {
        List<String> searchers = List.of("google", "yandex");
        List<String> hosts = searchers.stream().map(searcher -> getHost(PORT1, searcher))
                .collect(toCollection(ArrayList::new));

        String slowSearcher = "bing";
        String slowHost = getHost(PORT2, slowSearcher);
        hosts.add(slowHost);

        String query = "super query";

        try (StubServer fastStubServer = new StubServer(PORT1, searchers)) {
            try (StubServer slowStubServer =
                         new StubServer(PORT2, List.of(slowSearcher), defaultTimeout.plusSeconds(1))) {
                try (StubServer clientServer = new StubServer(CLIENT_PORT)) {
                    ActorSystem system = ActorSystem.create("TestSystem");

                    ActorRef master = system.actorOf(
                            Props.create(MasterActor.class, hosts, defaultTimeout), "master"
                    );

                    Object message = PatternsCS.ask(master, new Request(TOP, query), askTimeout.toMillis())
                            .toCompletableFuture().join();

                    assertTrue(message instanceof Map);
                    Map<String, Response> actualResponses = (Map<String, Response>) message;

                    assertEquals(searchers.size(), actualResponses.size());

                    for (Map.Entry<String, Response> entry : actualResponses.entrySet()) {
                        assertTrue(searchers.contains(entry.getKey()));
                        assertEquals(StubServer.createResponse(entry.getKey(), query, TOP), entry.getValue());
                    }
                }
            }
        }
    }
}

package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.SseEventSource;

import org.glassfish.jersey.internal.guava.ThreadFactoryBuilder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class TestApp extends JerseyTest {

    private final ExecutorService executorService =
            Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("ServerSentEventsTest-%d").build());
    private final Client client = ClientBuilder.newBuilder().executorService(executorService).build();

    @Override
    protected Application configure() {
        // enable(TestProperties.LOG_TRAFFIC);
        return new ResourceConfig(JaxRsServerSentEventsResource.class, DomainResource.class);
    }

    @Override
    protected Client getClient() {
        return client;
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        client.close();
        executorService.shutdown();
    }

    /**
     * Test consuming a single SSE event via event source.
     *
     * @throws Exception in case of a failure during the test execution.
     */
    @Test
    public void testEventSource() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<String> message = new AtomicReference<>();
        final SseEventSource eventSource = SseEventSource.target(target().path(App.ROOT_PATH)).build();
        eventSource.register((inboundEvent) -> {
            final String value = inboundEvent.readData();
            message.set(value);
            latch.countDown();
        });

        eventSource.open();
        target().path(App.ROOT_PATH).request().post(Entity.text("message")).close();

        try {
            assertTrue("Waiting for message to be delivered has timed out.",
                    latch.await(5 * getAsyncTimeoutMultiplier(), TimeUnit.SECONDS));
        } finally {
            target().path(App.ROOT_PATH).request().delete().close();
            eventSource.close();
        }
        assertEquals("Unexpected SSE event data value.", message.get(), "message");
    }


    /**
     * Test receiving all streamed messages in parallel by multiple event sources.
     *
     * @throws Exception in case of a failure during the test execution.
     */
    @Test
    public void testCreateDomain() throws Exception {
        final int MAX_CLIENTS = 25;
        final int MESSAGE_COUNT = 6;

        final Response response = target().path("domain/start")
                .queryParam("testSources", MAX_CLIENTS)
                .request().post(Entity.text("data"), Response.class);

        assertEquals("Unexpected start domain response status code.",
                response.getStatus(), Response.Status.CREATED.getStatusCode());

        final Map<Integer, Integer> messageCounts = new ConcurrentHashMap<>(MAX_CLIENTS);
        final CountDownLatch doneLatch = new CountDownLatch(MAX_CLIENTS);
        final SseEventSource[] sources = new SseEventSource[MAX_CLIENTS];

        final String processUriString = target().getUri().relativize(response.getLocation()).toString();

        final WebTarget sseTarget = target().path(processUriString).queryParam("testSource", "true");
        for (int i = 0; i < MAX_CLIENTS; i++) {
            final AtomicInteger messageCount = new AtomicInteger(0);  // will this work?
            final int id = i;
            sources[id] = SseEventSource.target(sseTarget).build();
            sources[id].register((event) -> {
                messageCount.incrementAndGet();
                final String message = event.readData(String.class);
                if ("done".equals(message)) {
                    messageCounts.put(id, messageCount.get());
                    doneLatch.countDown();
                }
            });
            sources[i].open();
        }

        doneLatch.await(5 * getAsyncTimeoutMultiplier(), TimeUnit.SECONDS);

        for (SseEventSource source : sources) {
            source.close();
        }

        for (int i = 0; i < MAX_CLIENTS; i++) {
            final Integer count = messageCounts.get(i);
            assertNotNull("Final message not received by event source " + i, count);
            assertEquals("Unexpected number of messages received by event source " + i,
                    count, new Integer(MESSAGE_COUNT));
        }
    }
}



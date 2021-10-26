package test;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

/**
 * Server sent event example.
 *
 * @author Pavel Bucek
 * @author Adam Lindenthal
 */
public class App {

    private static final URI BASE_URI = URI.create("http://localhost:8080/");
    static final String ROOT_PATH = "server-sent-events";

    public static void main(String[] args) {
        try {
            System.out.println("\"JAX-RS 2.1 Server-Sent Events\" Jersey Example App");

            //final ResourceConfig resourceConfig = new ResourceConfig(JaxRsServerSentEventsResource.class);

            final HttpServer server = JdkHttpServerFactory.createHttpServer(getBaseURI(), getResourceConfig());
            //final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig, false);
            //Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Server stop");
                    server.stop(0);
                }
            }));
            //server.start();

            System.out.println(String.format("Application started.\nTry out %s%s\nStop the application using CTRL+C",
                    BASE_URI, ROOT_PATH));

            Thread.currentThread().join();
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static ResourceConfig getResourceConfig() {
        
        Set<Class<?>> c = new HashSet<Class<?>>();
        c.add( JaxRsServerSentEventsResource.class );
        Set<Class<?>> classes = Collections.unmodifiableSet( c );
        ResourceConfig config = new ResourceConfig(classes);

//        config.property(ServerProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
//        config.property(ServerProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
        return config;
    }
    
    public static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost/").port(8080).build();
    }
}

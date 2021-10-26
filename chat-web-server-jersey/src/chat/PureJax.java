package chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class PureJax {
    

    /**
     * Starts the lightweight HTTP server serving the JAX-RS application.
     *
     * @return new instance of the lightweight HTTP server
     * @throws IOException
     */
    static HttpServer startServer() throws IOException {
        // create a new server listening at port 8080
        //HttpServer server = JdkHttpServerFactory.createHttpServer(getBaseURI(), getAppConfig());
        HttpServer server = JdkHttpServerFactory.createHttpServer(getBaseURI(), getResourceConfig());
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Server stop");
                server.stop(0);
            }
        }));
        
        return server;
    }
    
    static HttpServer startServerExample() throws IOException {
        // create a new server listening at port 8080
        final HttpServer server = HttpServer.create(new InetSocketAddress(getBaseURI().getPort()), 0);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                server.stop(0);
            }
        }));

        // create a handler wrapping the JAX-RS application
        HttpHandler handler = RuntimeDelegate.getInstance().createEndpoint(new JaxRsApplication(), HttpHandler.class);

        // map JAX-RS handler to the server root
        server.createContext(getBaseURI().getPath(), handler);

        // start the server
        server.start();

        return server;
    }
    
    static ResourceConfig getResourceConfig() {
        
        Set<Class<?>> c = new HashSet<Class<?>>();
        c.add( HelloResource.class );
        c.add( UserResource.class );
        c.add( ContactResource.class );
        c.add( MessageServerIndexResource.class );
        c.add( ConversationResource.class );
        //c.add( SSE2MessageResource.class );
        //c.add( PushMessageResource.class );
        c.add( WebpageResource.class );
        c.add( SessionResource.class );
        Set<Class<?>> classes = Collections.unmodifiableSet( c );
        ResourceConfig config = new ResourceConfig(classes);
        config.register(JacksonFeature.class);

        config.property(ServerProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
        config.property(ServerProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
        return config;
    }
    
    private static ResourceConfig getAppConfig() {
        ResourceConfig config = new ResourceConfig();
        Map<String, Object> initParams = new HashMap<>();
        System.out.println("App class "+JaxRsApplication.class);
//        initParams.put( "javax.ws.rs.Application", JaxRsApplication.class);
        config.property("javax.ws.rs.Application",JaxRsApplication.class);

        config.addProperties( initParams );
        return config;
        
        //public static ResourceConfig createJaxRsApp() {
        //    return new ResourceConfig(new MyApplication().getClasses());
        //}
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("\"Hello World\" Jersey Example Application");

        startServer();
        //startServerExample();

        System.out.println("Application started.\n"
                + "Try accessing " + getBaseURI() + "hello in the browser.\n"
                + "CTRL + C to stop the application...\n");

        //Thread.currentThread().join();
    }

    private static int getPort(int defaultPort) {
        final String port = System.getProperty("jersey.config.test.container.port");
        if (null != port) {
            try {
                return Integer.parseInt(port);
            } catch (NumberFormatException e) {
                System.out.println("Value of jersey.config.test.container.port property"
                        + " is not a valid positive integer [" + port + "]."
                        + " Reverting to default [" + defaultPort + "].");
            }
        }
        return defaultPort;
    }

    /**
     * Gets base {@link URI}.
     *
     * @return base {@link URI}.
     */
    public static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost/").port(getPort(8080)).build();
    }

}

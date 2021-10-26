package chat;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.simple.SimpleContainerFactory;
import org.glassfish.jersey.simple.SimpleServer;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;

public class SimpleWebServer implements Container{

    public static void main( String[] args ) {
        try {
            new SimpleWebServer().go();
        } catch( URISyntaxException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    public static void startServer() {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(8080).build();
//      URI baseUri = new URI("http", "localhost", "",
//              "", null);
      // uri.toURL().toString();
      SimpleContainerFactory.create(baseUri, getResourceConfig());
    }
    
    static ResourceConfig getResourceConfig() {
        
        Set<Class<?>> c = new HashSet<Class<?>>();
        c.add( HelloResource.class );
        c.add( UserResource.class );
        c.add( ContactResource.class );
        c.add( MessageServerIndexResource.class );
        c.add( ConversationResource.class );
        c.add( SSE2MessageResource.class );
        c.add( WebpageResource.class );
        c.add( SessionResource.class );
        Set<Class<?>> classes = Collections.unmodifiableSet( c );
        ResourceConfig config = new ResourceConfig(classes);
        config.register(JacksonFeature.class);

//        config.property(ServerProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
//        config.property(ServerProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
        return config;
    }
    
    private void go() throws URISyntaxException {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(8080).build();
//        URI baseUri = new URI("http", "localhost", "",
//                "", null);
        // uri.toURL().toString();
        ResourceConfig config = new ResourceConfig(HelloResource.class);
        SimpleServer server = SimpleContainerFactory.create(baseUri, config);
        
        System.out.println("Application started.");


    }
    
    private void go2() throws URISyntaxException {
        System.out.println("\"Hello World\" Jersey Example App");

        Map<String, Object> initParams = new HashMap<>();
        initParams.put(
                ServerProperties.PROVIDER_PACKAGES,
                SimpleWebServer.class.getPackage().getName());
        //final HttpServer server = GrizzlyWebContainerFactory.create(BASE_URI, ServletContainer.class, initParams);
        URI baseUri = new URI("http", "localhost", "",
                "", null);
        // uri.toURL().toString();
        ResourceConfig config = new ResourceConfig(SimpleWebServer.class);
        //config.addProperties( initParams );
        SimpleServer server = SimpleContainerFactory.create(baseUri, config);
//            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    server.shutdownNow();
//                }
//            }));
        

    }

    @Override
    public void handle(Request request, Response response) {
        try {
           PrintStream body = response.getPrintStream();
           long time = System.currentTimeMillis();
     
           response.setValue("Content-Type", "text/plain");
           response.setValue("Server", "HelloWorld/1.0 (Simple 4.0)");
           response.setDate("Date", time);
           response.setDate("Last-Modified", time);
     
           body.println("Hello World");
           body.close();
        } catch(Exception e) {
           e.printStackTrace();
        }
     } 


}

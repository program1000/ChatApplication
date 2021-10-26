package chat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;

@Path( "message" )
@Singleton
public class PushMessageResource {
    //uses Jersey JAX-RS (2.0)
    
    final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
    private Map<Integer, EventOutput> connections;
    private int seq;
    
    public PushMessageResource() {
        connections = new HashMap<Integer, EventOutput>();
        eventBuilder.name("test");
        seq=1;
    }
    
    @Path( "register" )
    @GET
    @Produces( MediaType.SERVER_SENT_EVENTS )
    public EventOutput register() {
        EventOutput connection = new EventOutput();
        connections.put( seq, connection );
        seq++;
//        try {
//            Thread.sleep( 5000 );
//        } catch( InterruptedException e ) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        return connection;
    }
    
    @Path( "test" )
    @GET
    @Produces( MediaType.SERVER_SENT_EVENTS )
    public EventOutput test() {
        EventOutput eventOutput = new EventOutput();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        // ... code that waits 1 second
                        final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
                        eventBuilder.name("message-to-client");
                        eventBuilder.data(String.class, "Hello world " + i + "!");
                        final OutboundEvent event = eventBuilder.build();
                        eventOutput.write(event);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error when writing the event.", e);
                } finally {
                    try {
                        eventOutput.close();
                    } catch (IOException ioClose) {
                        throw new RuntimeException("Error when closing the event output.", ioClose);
                    }
                }
            }
        }).start();
        return eventOutput;
    }
    
    @Path( "new")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response message( String content) {
        int key = Integer.valueOf( content.substring( 1,2 ) );
        for( Entry<Integer, EventOutput> e : connections.entrySet() ) {
            if ( e.getKey() != key ) {
                try {
                    e.getValue().write( ( createEvent( content.substring( 3 ), key ) ) );
                } catch( IOException e1 ) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
        return Response.ok("Send message for key "+key).build();
    }
    
    private OutboundEvent createEvent( String message, int key ) {
        eventBuilder.name("test-"+key);
        eventBuilder.data(String.class, message );
        final OutboundEvent event = eventBuilder.build();
        return event;
    }
    
    public void close ( String content ) {
        int id = Integer.valueOf( content );
        try {
            connections.get( id ).close();
        } catch( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

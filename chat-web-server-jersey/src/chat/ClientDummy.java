package chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.SseEventSource;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;

import chat.message.model.ChatMessage;
import chat.message.model.ContactInfo;
import chat.message.model.ConversationMessage;
import chat.message.model.SessionMessage;
import chat.message.model.TokenMessage;


//JDK client
public class ClientDummy {
    
    private WebTarget target;
    private WebTarget sseTarget;
    private SseEventSource sseHook;
    private EventSource eventSource;
    private ClientHeaderFilter filter;
    
    private int userId;
    
    public ClientDummy( int newUserId ) {
        String hostUrl = "http://localhost:8080";
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        target = client.target(hostUrl);
        userId=newUserId;
        
        filter = new ClientHeaderFilter(userId);
        Client sseClient = ClientBuilder.newClient().register( filter );
        sseClient.register(JacksonFeature.class);
        sseTarget = sseClient.target( hostUrl );
        
    }
    
    public String getUser( String path ) {
//      ClientConfig clientConfig = new ClientConfig();
        
        WebTarget newTarget = target.path(path);
        Form form = new Form();
        form.param( "username", "n"+userId );
        form.param( "password", "p"+userId );
        Response r = newTarget.request(MediaType.APPLICATION_JSON).post(Entity.form( form ));
        SessionMessage message = r.readEntity( SessionMessage.class );
        String sessionId = message.getSessionId();
        filter.setViewId( message.getUserId() );
        filter.setSessionId( message.getSessionId() );
        return "s"+ sessionId;
    }
    
    public String getInvite( String path ) {
        WebTarget newTarget = sseTarget.path(path);
        Response r = newTarget.request(MediaType.TEXT_PLAIN).post(Entity.entity( "", MediaType.TEXT_PLAIN ));
        return r.readEntity( String.class );
    }
    
    public String addContact( String path, String contactId ) {
        WebTarget newTarget = sseTarget.path(path);
        Response r = newTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(contactId, MediaType.TEXT_PLAIN ));
        ContactInfo ci = r.readEntity( ContactInfo.class );
        return ci.getId() + " "+ci.getName();
    }
    
    public String getMessageServer( String path, String sessionId, String name ) {
        WebTarget newTarget = target.path(path);
        Response r = newTarget.request(MediaType.TEXT_PLAIN).post(Entity.entity( sessionId+name, MediaType.TEXT_PLAIN ));
        return r.readEntity( String.class );
    }
    
    public String get( String path) {
        WebTarget newTarget = target.path(path);
        Response r = newTarget.request(MediaType.TEXT_PLAIN).get();
        return r.readEntity( String.class );
    }
    
    public String getJson( String path) {
        WebTarget newTarget = target.path(path);
        Response r = newTarget.request(MediaType.APPLICATION_JSON).get();
        return r.readEntity( String.class );
    }
    
    public String send( String path, String data ) {
        WebTarget newTarget = sseTarget.path(path);
        Response r = newTarget.request(MediaType.TEXT_PLAIN).post(Entity.entity( data, MediaType.TEXT_PLAIN ));
        return r.readEntity( String.class );
    }
    
    public String sendConv( String path, ConversationMessage message ) {
        WebTarget newTarget = sseTarget.path(path);
        Response r = newTarget.request(MediaType.TEXT_PLAIN).post(Entity.entity( message, MediaType.APPLICATION_JSON ));
        String convId = r.readEntity( String.class );
        filter.setConvId( convId );
        return convId;
    }
    
    public String socketMessage( String message ) throws IOException, InterruptedException, ExecutionException {
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8090);
        Future<Void> future = client.connect(hostAddress);

        future.get();


            byte[] byteMsg = String.valueOf(message.length()).getBytes();
            System.out.println("bL "+byteMsg.length);
            ByteBuffer buffer = ByteBuffer.wrap(byteMsg);
            System.out.println("bL2 "+buffer.capacity());
            Future<Integer> writeResult = client.write(buffer);

            // do some computation

            int r = writeResult.get();
            System.out.println("L1 "+r);
            buffer.flip();
            Future<Integer> readResult = client.read(buffer);
            
            // do some computation

            r = readResult.get();
            System.out.println("R1 "+r);
            buffer.flip();
            String echo = new String(buffer.array()).trim();
            buffer.clear();
            
            byteMsg = new String(message).getBytes();
            ByteBuffer buffer2 = ByteBuffer.wrap(byteMsg);
            Future<Integer> writeResult2 = client.write(buffer2);
            int reLen = writeResult2.get();
            System.out.println("MR "+reLen);
            
            return echo;

    }
    
    public String sendSSE( String path, String data ) {
        WebTarget newTarget = sseTarget.path(path);
        Response r = newTarget.request(MediaType.TEXT_PLAIN).post(Entity.entity( data, MediaType.TEXT_PLAIN ));
        return r.readEntity( String.class );
    }
    
    public String sendSSEToken( String path, TokenMessage message ) {
        WebTarget newTarget = sseTarget.path(path);
        Response r = newTarget.request(MediaType.TEXT_PLAIN).post(Entity.entity( message, MediaType.APPLICATION_JSON ));
        return r.readEntity( String.class );
    }
    
    public void registerSSE2( String path ) {
        WebTarget newTarget = target.path(path);
        SseEventSource sseEventSource = SseEventSource.target(newTarget).build();
        sseEventSource.register(event -> System.out.println( userId + "> "+event.getName() + "; "
            + event.readData(String.class)), error -> System.out.println("ERR: "+error.getMessage()));
        sseEventSource.open();
         
        // do other stuff, block here and continue when done
        sseHook = sseEventSource;

        //sseEventSource.close();
    }
    
    public void registerSSE( String path, String token ) {
        WebTarget newTarget = target.path(path);

        eventSource = new EventSource(newTarget) {
            @Override
            public void onEvent(InboundEvent inboundEvent) {
                
               if (inboundEvent.getName().equals( "token" )) {
                   ChatMessage in = inboundEvent.readData(ChatMessage.class, MediaType.APPLICATION_JSON_TYPE);
                   TokenMessage message = new TokenMessage( token, in.getContent() );
                   sendSSEToken( "message/accept", message );
               } else {
                   String content = inboundEvent.readData(String.class);
                   System.out.println(userId+": "+inboundEvent.getName() + "; " + content);
               }
                
            }
        };

        
//        EventListener listener = new EventListener() {
//            @Override
//            public void onEvent(InboundEvent inboundEvent) {
//                System.out.println(inboundEvent.getName() + "; " + inboundEvent.readData(String.class));
//            }
//        };
//        eventSource.register(listener, "test-"+userId);
//        eventSource.register(listener);
//        eventSource.open();
        
        //eventSource.close();
    }
    
    public void closeSSE2() {
        sseHook.close();
    }
    
    public void closeSSE() {
        eventSource.close();
    }
    
    private String nioExampleMessage(String message) throws Exception {
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        byte[] byteMsg = new String(message).getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(byteMsg);
        Future<Integer> writeResult = client.write(buffer);

        // do some computation

        writeResult.get();
        buffer.flip();
        Future<Integer> readResult = client.read(buffer);
        
        // do some computation

        readResult.get();
        String echo = new String(buffer.array()).trim();
        buffer.clear();
        return echo;
    }


}

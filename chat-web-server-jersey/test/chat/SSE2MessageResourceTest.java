package chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.junit.Test;

import chat.message.model.ChatMessage;
import chat.message.model.ConversationMessage;
import chat.message.model.TokenCreateMessage;
import chat.message.model.TokenMessage;

public class SSE2MessageResourceTest extends AbstractResource {
    
    private SessionIdFilter filter;
    
    protected void additionalClientSetup( Client client) {
        filter = new SessionIdFilter();
        client.register( filter );
    }
    
    @Test
    public void requestTest() {
        filter.setId( "1" );
        Response response = target( "message/request" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( "" ));
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        String message= response.readEntity(String.class);
        assertEquals( "1T", message );
    }
    
    @Test
    public void registerTest() {
        CaptureSSE eventCapture = new CaptureSSE( target( "message/register" ) );
        assertEquals( "1", eventCapture.answer() );
    }
    
    @Test
    public void acceptTest() {
        filter.setId( "1" );
        filter.setSessionId( "S" );
        Response requestResponse = target( "message/request" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( "" ));
        assertTrue( requestResponse.getStatus() == Status.OK.getStatusCode() );
        String requestToken = requestResponse.readEntity(String.class);
        assertEquals( "1T", requestToken );
        
        CaptureSSE eventCapture = new CaptureSSE( target( "message/register" ) );
        String responseToken = eventCapture.answer();
        assertEquals( "1", responseToken );
        TokenMessage message = new TokenMessage( requestToken, responseToken );
        
        Response acceptResponse = target( "message/accept" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.entity( message, MediaType.APPLICATION_JSON_TYPE ));
        assertTrue( acceptResponse.getStatus() == Status.NO_CONTENT.getStatusCode() );
        
        Response requestResponse2 = target( "message/request" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( "" ));
        assertTrue( requestResponse2.getStatus() == Status.OK.getStatusCode() );
        String requestToken2 = requestResponse2.readEntity(String.class);
        assertEquals( "1T", requestToken2 );
        
        CaptureSSE eventCapture2 = new CaptureSSE( target( "message/register" ) );
        String responseToken2 = eventCapture2.answer();
        assertEquals( "2", responseToken2 );
        TokenMessage message2 = new TokenMessage( requestToken2, responseToken2 );
        
        List<String> delayedMessage = new ArrayList<String>();
        String sender = "sender1";
        String convId = "conv1";
        String content = "content1";
        delayedMessage.add( sender );
        delayedMessage.add( convId );
        delayedMessage.add( content );
        securityManager.setDelayedMessage( delayedMessage );
        Response acceptResponse2 = target( "message/accept" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.entity( message2, MediaType.APPLICATION_JSON_TYPE ));
        assertTrue( acceptResponse2.getStatus() == Status.NO_CONTENT.getStatusCode() );
        
        ChatMessage cm = eventCapture2.message();
        assertNotNull( cm );
        assertEquals( sender, cm.getSender() );
        assertEquals( convId, cm.getConvId() );
        assertEquals( content, cm.getContent() );
    }
    
    @Test
    public void contactsTest() {
        filter.setId( "i" );
        filter.setConvId( "C" );
        Response requestResponse = target( "message/contacts" ).request(MediaType.APPLICATION_JSON_TYPE).post(Entity.text( "" ));
        assertTrue( requestResponse.getStatus() == Status.OK.getStatusCode() );
        ConversationMessage message = requestResponse.readEntity(ConversationMessage.class);
        assertNotNull( message );
        List<String> response = message.getContactIds();
        assertNotNull( response );
        assertEquals( 1, response.size() );
        assertEquals( "1iC", response.get( 0 ) );
    }
    
    @Test
    public void newTest() {
        filter.setId( "1" );
        filter.setConvId( "C" );
        Response requestResponse = target( "message/request" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( "" ));
        assertTrue( requestResponse.getStatus() == Status.OK.getStatusCode() );
        String requestToken = requestResponse.readEntity(String.class);
        assertEquals( "1T", requestToken );
        
        CaptureSSE eventCapture = new CaptureSSE( target( "message/register" ) );
        String responseToken = eventCapture.answer();
        assertEquals( "1", responseToken );
        TokenMessage message = new TokenMessage( requestToken, responseToken );
        
        Response acceptResponse = target( "message/accept" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.entity( message, MediaType.APPLICATION_JSON_TYPE ));
        assertTrue( acceptResponse.getStatus() == Status.NO_CONTENT.getStatusCode() );

        String content = "content";
        Response newResponse = target( "message/new" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( content ));
        assertTrue( newResponse.getStatus() == Status.OK.getStatusCode() );
        ChatMessage cm = eventCapture.message();
        assertNotNull( cm );
        assertEquals( "from1", cm.getSender() );
        assertEquals( "C", cm.getConvId() );
        assertEquals( content, cm.getContent() );
        
        assertEquals( "C1"+content, securityManager.getReceivedMessage());
        
        // test delayed message
        String offLineContent = "offline content";
        securityManager.setOffLineTarget();
        Response offLineResponse = target( "message/new" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( offLineContent ));
        assertTrue( offLineResponse.getStatus() == Status.OK.getStatusCode() );
        assertEquals( "C111"+offLineContent, securityManager.getDelayedMessage() );
        
        assertEquals( "C1"+offLineContent, securityManager.getReceivedMessage());
    }
    
    @Test
    public void closeTest() {
        filter.setId( "1" );
        filter.setConvId( "C" );
        Response requestResponse = target( "message/request" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( "" ));
        assertTrue( requestResponse.getStatus() == Status.OK.getStatusCode() );
        String requestToken = requestResponse.readEntity(String.class);
        assertEquals( "1T", requestToken );
        
        CaptureSSE eventCapture = new CaptureSSE( target( "message/register" ) );
        String responseToken = eventCapture.answer();
        assertEquals( "1", responseToken );
        TokenMessage message = new TokenMessage( requestToken, responseToken );
        
        Response acceptResponse = target( "message/accept" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.entity( message, MediaType.APPLICATION_JSON_TYPE ));
        assertTrue( acceptResponse.getStatus() == Status.NO_CONTENT.getStatusCode() );
        
        Response closeResponse = target( "message/close" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( "" ));
        String answer = closeResponse.readEntity(String.class);
        assertEquals( "Closed 1", answer );
        
        String content = "content";
        Response newResponse = target( "message/new" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( content ));
        assertTrue( newResponse.getStatus() == Status.OK.getStatusCode() );
        assertEquals( "C11"+content, securityManager.getDelayedMessage() );
        
        Response alreadyClosedResponse = target( "message/close" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( "" ));
        String doubleAnswer = alreadyClosedResponse.readEntity(String.class);
        assertEquals( "Already closed 1", doubleAnswer );
        
    }
    
    class CaptureSSE extends EventSource {
        
        private ArrayBlockingQueue<String> queue;
        private ArrayBlockingQueue<ChatMessage> messageQueue;

        public CaptureSSE( WebTarget endpoint ) {
            super( endpoint );
            queue = new ArrayBlockingQueue<String>( 1 );
            messageQueue = new ArrayBlockingQueue<ChatMessage>( 1 );
        }
        
        @Override
        public void onEvent(InboundEvent inboundEvent) {
            
           if (inboundEvent.getName().equals( "token" )) {
               TokenCreateMessage in = inboundEvent.readData(TokenCreateMessage.class, MediaType.APPLICATION_JSON_TYPE);
               queue.add( in.token );
           } else {
               ChatMessage in = inboundEvent.readData( ChatMessage.class, MediaType.APPLICATION_JSON_TYPE);
               messageQueue.add( in );
           }
            
        }
        
        public String answer() {
            try {
                return queue.poll( 20, TimeUnit.SECONDS );
            } catch( InterruptedException e ) {
               return null;
            }
        }
        
        public ChatMessage message() {
            try {
                return messageQueue.poll( 20, TimeUnit.SECONDS );
            } catch( InterruptedException e ) {
               return null;
            }
        }
        
    }

}

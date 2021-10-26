package chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import chat.message.model.ConversationMessage;

public class ConversationResourceTest extends AbstractResource {
    
    private SessionIdFilter filter;
    
    protected void additionalClientSetup( Client client) {
        filter = new SessionIdFilter();
        client.register( filter );
    }
    
    @Test
    public void newTest() {
        filter.setId( "i" );
        List<String> contactList = new ArrayList<String>();
        String contactId = "c1";
        contactList.add( contactId );
        Response response = target( "conversation/new" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.entity( new ConversationMessage(contactList), MediaType.APPLICATION_JSON_TYPE));
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        String message= response.readEntity(String.class);
        assertEquals( "1"+contactId+"i", message );
    }
    
    @Test
    public void closeTest() {
        Response response = target( "conversation/close" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( "" ));
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        boolean message= response.readEntity(Boolean.class);
        assertEquals( true, message );
        
        securityManager.setClosedFalse();
        Response notClosedResponse = target( "conversation/close" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( "" ));
        assertTrue( notClosedResponse.getStatus() == Status.OK.getStatusCode() );
        boolean notClosedMessage= notClosedResponse.readEntity(Boolean.class);
        assertEquals( false, notClosedMessage );
    }

}

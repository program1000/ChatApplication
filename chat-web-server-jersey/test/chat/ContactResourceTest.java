package chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import chat.connector.ConnectorRegister;
import chat.connector.ContactConnectorStub;
import chat.message.model.ContactInfo;
import chat.message.model.WarningMessage;

public class ContactResourceTest extends AbstractResource {
    
    private ContactConnectorStub connector;
    private SessionIdFilter filter;
    
    protected void setupConnectors() {
        connector = new ContactConnectorStub();
        ConnectorRegister.setContactConnector( connector );
    }
    
    protected void additionalClientSetup( Client client) {
        filter = new SessionIdFilter();
        client.register( filter );
    }
    
    @Test
    public void inviteTest() {
        filter.setSessionId( "s" );
        Response response = target( "contact/invite" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( "" ));
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        String message= response.readEntity(String.class);
        assertEquals( "s1", message );
    }
    
    @Test
    public void newTest() {
        filter.setSessionId( "s" );
        filter.setId( "i" );
        String token="t";
        Response response = target( "contact/new" ).request(MediaType.APPLICATION_JSON_TYPE).post(Entity.text( token ));
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        ContactInfo contactInfo = response.readEntity(ContactInfo.class);
        assertEquals( "1", contactInfo.getId() );
        assertEquals( "is"+token, contactInfo.getName() );
        
        securityManager.setError();
        Response noContactResponse = target( "contact/new" ).request(MediaType.APPLICATION_JSON_TYPE).post(Entity.text( token ));
        assertTrue( noContactResponse.getStatus() == Status.OK.getStatusCode() );
        WarningMessage message = noContactResponse.readEntity(WarningMessage.class);
        assertEquals( "is"+token, message.getWarning() );
     }
    
    @Test
    public void deleteTest() {
        filter.setSessionId( "s" );
        filter.setId( "i" );
        String id="1";
        Response response = target( "contact/del" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( id ));
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        String message= response.readEntity(String.class);
        assertEquals( "Removed true", message );
        
        securityManager.setRemovedFalse();
        Response notRemovedResponse = target( "contact/del" ).request(MediaType.TEXT_PLAIN_TYPE).post(Entity.text( id ));
        assertTrue( notRemovedResponse.getStatus() == Status.OK.getStatusCode() );
        String notRemovedMessage= notRemovedResponse.readEntity(String.class);
        assertEquals( "Removed false", notRemovedMessage );
    }

}

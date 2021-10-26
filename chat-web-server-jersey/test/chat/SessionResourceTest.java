package chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import chat.connector.AccountServerConnectorStub;
import chat.connector.ConnectorRegister;
import chat.connector.SecurityManagerStub;
import chat.message.model.ErrorMessage;
import chat.message.model.LoginMessage;
import chat.message.model.SessionMessage;

public class SessionResourceTest extends AbstractResource {
    
    private AccountServerConnectorStub accountConnector;
    
    protected void setupConnecters() {
        accountConnector = new AccountServerConnectorStub();
        ConnectorRegister.setAccountConnector( accountConnector );
    }

    @Test
    public void newSessionTest() {
        Response response = target( "session/new" ).request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity( new LoginMessage("n1","p1"), MediaType.APPLICATION_JSON_TYPE));
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        SessionMessage message= response.readEntity(SessionMessage.class);
        assertEquals( "1", message.getSessionId() );
        
        securityManager.setError();
        response = target( "session/new" ).request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity( new LoginMessage("n1","p1"), MediaType.APPLICATION_JSON_TYPE));
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        ErrorMessage errMessage= response.readEntity(ErrorMessage.class);
        assertEquals( SecurityManagerStub.ERR_NO_LOGIN, errMessage.getReason() );
        
    }
}

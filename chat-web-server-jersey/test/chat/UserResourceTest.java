package chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import chat.connector.AccountConnectorInterface;
import chat.connector.AccountServerConnectorStub;
import chat.connector.ConnectorRegister;
import chat.connector.MessageServerConnectorStub;
import chat.message.model.ErrorMessage;
import chat.message.model.SessionMessage;
import chat.message.server.connector.MessageServerConnectorRegister;

public class UserResourceTest extends AbstractResource {
    
    private AccountServerConnectorStub accountConnector;
    private MessageServerConnectorStub messageConnector;

    protected void setupConnectors() {
        accountConnector = new AccountServerConnectorStub();
        ConnectorRegister.setAccountConnector( accountConnector );
        messageConnector = new MessageServerConnectorStub();
        MessageServerConnectorRegister.setAccountConnector( messageConnector );
    }
    
    @Test
    public void getTest() {
        Response response = target( "hello/response" ).request().get();
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        assertEquals( "TEST", response.readEntity(String.class) );
    }
    
    @Test
    public void createUserTest() {
        String url = "user/new";
        String user = "user";
        String pass = "pass";
        //Entity<String> params = Entity.<String>text( user + "," + pass );
        Form form = new Form();
        form.param( "username", user );
        form.param( "password", pass );

        //response with new user
        Response response = target(url).request(MediaType.APPLICATION_JSON_TYPE).post(  Entity.form( form ) );
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        SessionMessage message =  response.readEntity(SessionMessage.class);
        assertEquals( "1",  message.getSessionId() );
        assertNotNull( accountConnector );
        assertEquals( user, accountConnector.getUser() ); 
        assertEquals( pass, accountConnector.getPass() );
        assertNotNull( messageConnector );
        assertEquals( 1, messageConnector.getUserId() );
        
        //response with user already created
        accountConnector.setHasUser();
        response = target(url).request(MediaType.APPLICATION_JSON_TYPE).post(  Entity.form( form ) );
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        ErrorMessage errorMessage =  response.readEntity(ErrorMessage.class);
        assertEquals( "User [user] already exists", errorMessage.getReason() );
        assertNotNull( accountConnector );
        assertEquals( user, accountConnector.getUser() ); 
        assertEquals( pass, accountConnector.getPass() );
    }
    
    @Test
    public void authenticateUserTest() {
        String url = "user/auth";
        String user = "user";
        String pass = "pass";
        Form form = new Form();
        form.param( "username", user );
        form.param( "password", pass );

        // response with authenticated user
        Response response = target( url ).request().post( Entity.form( form ) );
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        assertEquals( "1", response.readEntity( String.class ) );
        assertNotNull( accountConnector );
        assertEquals( user, accountConnector.getUser() );
        assertEquals( pass, accountConnector.getPass() );

        // response with unauthenticated user
        accountConnector.setFailAuthenticateUser();
        response = target( url ).request().post( Entity.form( form ) );
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        assertEquals( AccountConnectorInterface.NO_SESSION_ID, response.readEntity( String.class ) );
        assertNotNull( accountConnector );
        assertEquals( user, accountConnector.getUser() );
        assertEquals( pass, accountConnector.getPass() );

    }
    
    //post example
//    Response response = target("fruit/created").request()
//            .post(Entity.json("{\"name\":\"strawberry\",\"weight\":20}"));
//
//        assertEquals("Http Response should be 201 ", Status.CREATED.getStatusCode(), response.getStatus());
//        assertThat(response.readEntity(String.class), containsString("Fruit saved : Fruit [name: strawberry colour: null]"));
}

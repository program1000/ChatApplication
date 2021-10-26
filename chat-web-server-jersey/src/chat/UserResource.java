package chat;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import chat.connector.AccountConnector;
import chat.connector.AccountConnectorInterface;
import chat.connector.ConnectorRegister;
import chat.message.model.ErrorMessage;
import chat.message.model.ResponseMessage;
import chat.message.model.SessionMessage;
import chat.message.server.connector.MessageServerAccountConnector;
import chat.message.server.connector.MessageServerConnectorRegister;

@Path("user")
@Singleton
public class UserResource {
    
    private AccountConnector connector = ConnectorRegister.getAccountConnector();
    private MessageServerAccountConnector messageServerAccountConnector = MessageServerConnectorRegister.getAccountConnector();

    @Path("new")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMessage createUser( @FormParam("username") String username, @FormParam("password") String password ) {
        String sessionId = connector.addUser( username, password );
        if ( sessionId == AccountConnectorInterface.NO_SESSION_ID ) {
            // already exists
            return new ErrorMessage( "User ["+username+"] already exists" );
        }
        int userId = connector.getUserIdFromSessionId( sessionId );
        boolean msResult = messageServerAccountConnector.addUser( userId );
        if ( !msResult ) {
           //error
            return new ErrorMessage( "User ["+username+"] already exists in Message Server" );
        }
        return new SessionMessage( null, sessionId);
    }
    
    @Path("auth")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String authenticateUser(@FormParam("username") String username, @FormParam("password") String password) {
        return connector.authenticateUser( username, password );
    }
    
}

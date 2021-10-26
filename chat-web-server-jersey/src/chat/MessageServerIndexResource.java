package chat;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import chat.connector.ConnectorRegister;
import chat.connector.MessageServerConnector;

@Path("messageserverindex")
@Singleton
public class MessageServerIndexResource {
    private MessageServerConnector connector = ConnectorRegister.getMessageServerConnector();
    
    @Path("default")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getContact( String content ) {
        String sessionId = content.substring( 1,2 );
        String name = content.substring( 3 );
        String result = connector.getDefaultMessageServer( sessionId, name );

        return Response.ok(result).build();
    }
}

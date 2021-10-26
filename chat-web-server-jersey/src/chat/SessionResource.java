package chat;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import chat.message.model.LoginMessage;
import chat.message.model.ResponseMessage;

@Singleton
@Path("session")
public class SessionResource {
    
    @Context
    private ResourceContext rc;
    private final SecurityManagerInterface securityManager = SecurityManager.get();

    @Path("new")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMessage submit( LoginMessage message ) {
        System.out.println("LOGIN");
        UserResource userResource = rc.getResource(UserResource.class);
        String sessionId = userResource.authenticateUser( message.getName(), message.getPass() );
        return securityManager.getNewSession( sessionId );
    }
}

package chat;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import chat.message.model.ConversationMessage;

@Path("conversation")
@Singleton
public class ConversationResource {
    
    private final SecurityManagerInterface securityManager = SecurityManager.get();
    
    @Path("new")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createConversation( @HeaderParam( value = "x-id" ) String viewId, ConversationMessage message ) {
        String convViewId = securityManager.createConversation( viewId, message.getContactIds() );
        return Response.ok(convViewId).build();
    }
    
    @Path("close")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response closeConversation( @HeaderParam( value = "x-id" ) String viewId, @HeaderParam( value = "x-conv-id" ) String convViewId ) {
        boolean hasRemoved = securityManager.closeConversation( viewId, convViewId );
        return  Response.ok( hasRemoved).build();
    }
}

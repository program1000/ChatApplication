package chat;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import chat.connector.ConnectorRegister;
import chat.connector.ContactConnector;
import chat.message.model.ResponseMessage;

@Path("contact")
@Singleton
public class ContactResource {
    
    private final ContactConnector connector = ConnectorRegister.getContactConnector();
    private final SecurityManagerInterface securityManager = SecurityManager.get();
    
    @Path("invite")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getContact( @HeaderParam( value = SecurityManagerInterface.HEADER_SESSION ) String sessionId ) {
        String result = connector.getContactId( sessionId );
        return Response.ok(result).build();
    }
    
    @Path("new")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMessage newContact( @HeaderParam( value = SecurityManagerInterface.HEADER_ID ) String viewId,
                                   @HeaderParam( value = SecurityManagerInterface.HEADER_SESSION ) String sessionId, 
                                   String contactTokenId ) {
        return securityManager.addContact( viewId, sessionId, contactTokenId );
    }
    
    @Path("del")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response delContact( @HeaderParam( value = SecurityManagerInterface.HEADER_ID ) String viewId,
                                @HeaderParam( value = SecurityManagerInterface.HEADER_SESSION ) String sessionId, 
                                String contactViewId ) {
        boolean result = securityManager.delContact( viewId, sessionId, contactViewId );

        return Response.ok("Removed "+result).build();
    }

}

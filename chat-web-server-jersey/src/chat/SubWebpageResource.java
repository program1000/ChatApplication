package chat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import chat.message.model.LoginMessage;
import chat.message.model.ResponseMessage;

@Singleton
public class SubWebpageResource {
    
    private FileManager fileManager;
    private Map<String, MediaType> mediaMap;
    @Context
    private ResourceContext resourceContext;
    
    public SubWebpageResource() {
        fileManager = new FileManager();
        mediaMap = new HashMap<String, MediaType>();
        mediaMap.put("html", MediaType.TEXT_HTML_TYPE );
        mediaMap.put("css", new MediaType( "text", "css" ));
        mediaMap.put("js", new MediaType( "application", "javascript"));
    }

    @Path("submit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMessage submit(LoginMessage message ) {
        return resourceContext.getResource(SessionResource.class).submit( message );
    }
    
    @Path("{target}")
    @GET
    @Produces(MediaType.WILDCARD)
    public Response getTarget( @Context Request request, @PathParam("target") String target) {
        Date lastMod = fileManager.getLastMod( target );
        if ( lastMod == null ) {
            //error
            return Response.status( Status.BAD_REQUEST ).entity(Entity.text("Not found: "+target)).build();
        }
        // returns if not modified
        ResponseBuilder rb = request.evaluatePreconditions( lastMod );
        if (rb != null) {
            throw new WebApplicationException(rb.build());
        }

        String result= fileManager.getContent( target );
        MediaType type = mediaMap.get( fileManager.getExtension( target ) );
        if ( type==null) {
            throw new WebApplicationException("Unknown mediatype for: "+target);
        }

        return Response.ok( result ).lastModified( lastMod ).type( type ).build();
    }

}

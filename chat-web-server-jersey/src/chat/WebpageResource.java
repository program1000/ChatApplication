package chat;

import javax.inject.Singleton;
import javax.ws.rs.Path;

@Path( "html" )
@Singleton
public class WebpageResource {

    @Path("/")
    public Class<SubWebpageResource> getItemContentResource() {
        return SubWebpageResource.class;
    }
}



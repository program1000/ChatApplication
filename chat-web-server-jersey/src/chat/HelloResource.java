package chat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("hello")
public class HelloResource {

      @GET
      @Path("response")
      @Produces(MediaType.TEXT_PLAIN)
      public Response testResponse() {
          System.out.println("GET Test with response");
          return Response.ok("TEST").build();
      }
}

package chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import chat.message.model.LoginMessage;
import chat.message.model.SessionMessage;

public class SubWebpageResourceTest extends AbstractResource {
    
    private LastModFilter lastModfilter;
    
    protected void additionalClientSetup( Client client) {
        lastModfilter = new LastModFilter();
        client.register( lastModfilter );
    }
    
    @Test
    public void sumbitTest() {
        Response response = target( "html/submit" ).request(MediaType.APPLICATION_JSON_TYPE).post(
                Entity.entity( new LoginMessage("n1","p1"), MediaType.APPLICATION_JSON_TYPE));
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        SessionMessage message= response.readEntity(SessionMessage.class);
        assertEquals( "1", message.getSessionId() );
    }
    
    @Test
    public void webpageLoadTest() {
        Response response = target( "html/status.js" ).request().get();
        assertTrue( response.getStatus() == Status.OK.getStatusCode() );
        String content = response.readEntity(String.class);
        assertTrue( content!=null&&content.length()>0);
        String lastMod = response.getHeaderString( "Last-Modified" );
        assertTrue( lastMod!=null&&lastMod.length()>0);
        
        
        //test not modified status when sending if modified since header
        lastModfilter.setLastMod( lastMod );
        Response lastModResponse = target( "html/status.js" ).request().get();
        assertTrue( lastModResponse.getStatus() == Status.NOT_MODIFIED.getStatusCode() );
        String lastModcontent = lastModResponse.readEntity(String.class);
        assertTrue( lastModcontent!=null&&lastModcontent.length()==0);
    
    }

}

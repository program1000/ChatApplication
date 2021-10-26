package chat;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

public class SessionIdFilter implements ClientRequestFilter {

    private String sessionId;
    private String id;
    private String convId;
    
    public void setSessionId( String sessionId ) {
        this.sessionId = sessionId;
    }
    
    public void setId( String id ) {
        this.id = id;
    }
    
    public void setConvId( String convId ) {
        this.convId = convId;
    }

    @Override
    public void filter( ClientRequestContext requestContext ) throws IOException {
        if( sessionId != null ) {
            requestContext.getHeaders().add( SecurityManagerInterface.HEADER_SESSION, sessionId );
        }
        if( id != null ) {
            requestContext.getHeaders().add( SecurityManagerInterface.HEADER_ID, id );
        }
        if( convId !=null ) {
            requestContext.getHeaders().add( SecurityManagerInterface.HEADER_CONV_ID, convId );
        }
    }

}

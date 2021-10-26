package chat;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

public class ClientHeaderFilter implements ClientRequestFilter {

    public static final String FILTER_HEADER_KEY = "x-id";
    public static final String FILTER_SESSION_HEADER_KEY = "x-session-id";
    public static final String FILTER_CONV_HEADER_KEY = "x-conv-id";
    private String idValue;
    private String sessionIdValue;
    private String convIdValue;
    
    public ClientHeaderFilter( int id ) {
        idValue = ""+id;
        sessionIdValue="";
        convIdValue="";
    }
    

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().add(FILTER_HEADER_KEY, idValue);
        requestContext.getHeaders().add(FILTER_SESSION_HEADER_KEY, sessionIdValue);
        requestContext.getHeaders().add(FILTER_CONV_HEADER_KEY, convIdValue);
    }
    
    public void setViewId( String viewId ) {
        idValue = viewId;
    }

    public void setSessionId( String sessionId ) {
        sessionIdValue = sessionId;
    }


    public void setConvId( String convId ) {
        convIdValue = convId;
    }
}


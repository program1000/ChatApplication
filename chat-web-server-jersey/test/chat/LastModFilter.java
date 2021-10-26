package chat;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

public class LastModFilter implements ClientRequestFilter {

    public static final String FILTER_HEADER_KEY = "If-Modified-Since";
    private String lastMod;
    
    public void setLastMod( String lastMod ) {
        this.lastMod = lastMod;
    }
    

    @Override
    public void filter( ClientRequestContext requestContext ) throws IOException {
        if( lastMod != null ) {
            requestContext.getHeaders().add( FILTER_HEADER_KEY, lastMod );
        }
    }

}

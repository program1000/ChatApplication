package chat.processor;

import java.util.List;

import chat.connector.MessageServerConnectorInterface;
import chat.model.MessageServer;
import chat.model.User;
import chat.request.GetDefaultMessageServerRequest;
import chat.request.GetMessageServerRequest;

public class MessageServerProcessor extends AbstractComponent implements MessageServerConnectorInterface {

	
	public void addMessageServer() {
		
	}
	
	public String getMessageServer( String sessionId, GetMessageServerRequest request ) {
	    User user = sessionProcessor.getUserFromSession( sessionId );
		//return ip and session token
	    List<MessageServer> servers = db.getMessageServers( user );
	    if ( servers.isEmpty() ) {
	        //error
	        return null;
	    }
	    return servers.get( 0 ).getHost();

	}
	
	public String getDefaultMessageServer( String sessionId, GetDefaultMessageServerRequest request ) {
	    User user = sessionProcessor.getUserFromSession( sessionId );
	    if ( user==null ) {
	        //error
	    }
	    List<MessageServer> servers = db.getDefaultMessageServers( request.getName() );
        if ( servers.isEmpty() ) {
            //error
            return null;
        }
        return servers.get( 0 ).getHost();
	}
}

package chat.connector;

import chat.request.GetDefaultMessageServerRequest;
import chat.request.GetMessageServerRequest;

public class MessageServerConnector {
    private MessageServerConnectorInterface messageServerConnector;
    
    public void setMessageServerConnector( MessageServerConnectorInterface connector ) {
        messageServerConnector = connector;
    }
    
    public boolean isInited() {
        return messageServerConnector!=null;
    }
    
    
    public String getMessageServer( String sessionId ) {
        GetMessageServerRequest request = new GetMessageServerRequest();
        return messageServerConnector.getMessageServer( sessionId, request );
    }
    
    public String getDefaultMessageServer( String sessionId, String name ) {
        GetDefaultMessageServerRequest request = new GetDefaultMessageServerRequest( name );
        return messageServerConnector.getDefaultMessageServer( sessionId, request );
    }
}

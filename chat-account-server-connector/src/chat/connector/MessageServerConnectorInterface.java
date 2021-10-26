package chat.connector;

import chat.request.GetDefaultMessageServerRequest;
import chat.request.GetMessageServerRequest;

public interface MessageServerConnectorInterface {

    
    String getMessageServer( String sessionId, GetMessageServerRequest request );
    String getDefaultMessageServer( String sessionId, GetDefaultMessageServerRequest request );
}

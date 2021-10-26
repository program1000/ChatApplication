package chat.connector;

import chat.request.AuthenticateRequest;
import chat.request.NewUserRequest;

public interface AccountConnectorInterface {
    
    final String NO_SESSION_ID="-1";
    
    String createUser( NewUserRequest request );

    String authenticate( AuthenticateRequest request );
    
    int getUserIdFromSessionId( String sessionId );

}